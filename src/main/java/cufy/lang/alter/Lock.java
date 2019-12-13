/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */

package cufy.lang.alter;

import cufy.lang.ConcurrentException;
import cufy.util.ObjectUtil;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A java lock locker thread. Designed to be a replacement of wrapping the code with 'synchronized' statement. By creating a thread on the background
 * to {@link #lock()} that lock and hold it until {@link #release()} get invoked.
 *
 * @param <T> the type of that lock
 * @author LSaferSE
 * @version 1 release (07-Dec-2019)
 * @since 07-Dec-2019
 */
public class Lock<T> extends Thread implements Closeable {
	/**
	 * The lock holder should end it's thread.
	 */
	final static protected int CLOSE = -1;
	/**
	 * The lock holder should gain it's targeted lock.
	 */
	final static protected int GAIN = 1;
	/**
	 * The lock holder should release it's targeted lock.
	 */
	final static protected int RELEASE = 0;
	/**
	 * The targeted lock.
	 */
	final protected T lock;
	/**
	 * The thread that have created this. To avoid serving other threads.
	 */
	final protected Thread master;
	/**
	 * The reference to communicate with the lock holder thread. Representing the state integer code.
	 */
	final protected AtomicInteger state = new AtomicInteger(CLOSE);

	/**
	 * Initialize a new lock holder.
	 *
	 * @param lock the targeted lock
	 * @throws NullPointerException if the given lock is null
	 */
	public Lock(T lock) {
		ObjectUtil.requireNonNull(lock, "lock");
		this.setDaemon(true);
		this.lock = lock;
		this.master = Thread.currentThread();
	}

	/**
	 * Initialise a new lock. Locking on itself.
	 */
	public Lock() {
		this.setDaemon(true);
		this.lock = (T) this;
		this.master = Thread.currentThread();
	}

	@Override
	public void close() {
		this.checkCaller(this.master);
		synchronized (this.state) {
			this.state.set(CLOSE);
			this.state.notify();
		}
		try {
			this.join();
		} catch (InterruptedException ignored) {
		}
	}

	@Override
	public void run() {
		this.checkCaller(this);
		while (true) {
			synchronized (this.state) {
				switch (this.state.get()) {
					case RELEASE:
						this.release0();
						break;
					case GAIN:
						this.lock0();
						break;
					case CLOSE:
						this.close0();
						return;
					default:
						throw new IllegalStateException(this.state.toString());
				}
			}
		}
	}

	/**
	 * Hold the lock. Wait for the lock been gained.
	 *
	 * @throws IllegalThreadStateException if this lock already closed
	 * @throws ConcurrentException         if the caller thread isn't the master of this
	 */
	public void lock() {
		this.checkCaller(this.master);
		synchronized (this.state) {
			if (!this.isAlive())
				this.start();
			try {
				this.state.set(GAIN);
				this.state.notify();
				this.state.wait();
			} catch (InterruptedException ignored) {
			}
		}
	}

	/**
	 * Release the targeted lock by this.
	 *
	 * @throws ConcurrentException if the caller thread isn't the master of this
	 */
	public void release() {
		this.checkCaller(this.master);
		synchronized (this.state) {
			if (!this.isAlive())
				return;
			try {
				this.state.set(RELEASE);
				this.state.notify();
				this.state.wait();
			} catch (InterruptedException ignored) {
			}
		}
	}

	/**
	 * Make sure that the caller thread is the master thread of this.
	 *
	 * @param master the master to check for
	 */
	protected void checkCaller(Thread master) {
		if (Thread.currentThread() != master)
			throw new ConcurrentException("Caller thread is not the creator of this lock");
	}

	/**
	 * Do code before closing the lock.
	 */
	protected void close0() {
		this.checkCaller(this);
		synchronized (this.state) {
			this.state.notify();
		}
	}

	/**
	 * Sleep with while owning the lock.
	 *
	 * @implSpec notify {@link #state} before sleeping
	 */
	protected void lock0() {
		this.checkCaller(this);
		synchronized (this.state) {
			synchronized (this.lock) {
				try {
					this.state.notify();
					this.state.wait();
				} catch (InterruptedException ignored) {
				}
			}
		}
	}

	/**
	 * Sleep until notified.
	 */
	protected void release0() {
		this.checkCaller(this);
		synchronized (this.state) {
			try {
				this.state.notify();
				this.state.wait();
			} catch (InterruptedException ignored) {
			}
		}
	}
}
