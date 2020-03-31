/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.cnv;

import cufy.lang.Clazz;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

@SuppressWarnings("JavaDoc")
public class BaseConverterTest {
	@Test
	public void array_array() {
		//deep conversion
		{
			int[][] input = {{1, 2}, {3, 6}, {0}, {7}, {1}};
			double[][] output = {{7, 3}, {7, 3}, {2}, {6}, {0}};

			BaseConverter.sconvert(new ConvertArguments(input, output));

			Assert.assertEquals("Value not changed", (double) input[0][0], output[0][0], 0);
			Assert.assertEquals("Value not changed", (double) input[0][1], output[0][1], 0);
			Assert.assertEquals("Value not changed", (double) input[1][0], output[1][0], 0);
			Assert.assertEquals("Value not changed", (double) input[1][1], output[1][1], 0);
			Assert.assertEquals("Value not changed", (double) input[2][0], output[2][0], 0);
			Assert.assertEquals("Value not changed", (double) input[3][0], output[3][0], 0);
			Assert.assertEquals("Value not changed", (double) input[4][0], output[4][0], 0);
		}
		//recurse test
		{
			Object[] input = {null};
			input[0] = input;

			Object[] output = {null};

			BaseConverter.sconvert(new ConvertArguments(input, output));

			Assert.assertSame("recursion not converted", output, output[0]);
		}
	}

	@Test
	public void array_collection() {
		//place then clear then place all components in the source
		{
			double[][] input1 = {{7, 3}, {7, 3}, {2}, {6}, {0}};
			int[][] input2 = {{1, 2}, {3, 6}, {0}, {7}, {1}};
			Collection output = new HashSet();

			BaseConverter.sconvert(new ConvertArguments<>(input1, output));
			BaseConverter.sconvert(new ConvertArguments<>(input2, output, Clazz.of(HashSet.class, Clazz.of(double[].class))));

			//TODO
		}
		//recurse test
		{
			Object[] input = {null};
			input[0] = input;

			Collection output = new HashSet();

			BaseConverter.sconvert(new ConvertArguments(input, output));

			Assert.assertSame("recursion not converted", output, output.iterator().next());
		}
	}

	@Test
	public void array_list() {
		//basic test
		{
			double[][] input0 = {{7, 3}, {7, 3}, {2}, {6}, {0}};
			int[][] input1 = {{1, 2}, {3, 6}, {0}, {7}, {1}};
			List output = new ArrayList(Arrays.asList(input0));

			BaseConverter.sconvert(new ConvertArguments<>(input1, output, Clazz.of(output, Clazz.of(double[].class))));

			Assert.assertEquals("Value not changed", (double) input1[0][0], input0[0][0], 0);
			Assert.assertEquals("Value not changed", (double) input1[0][1], input0[0][1], 0);
			Assert.assertEquals("Value not changed", (double) input1[1][0], input0[1][0], 0);
			Assert.assertEquals("Value not changed", (double) input1[1][1], input0[1][1], 0);
			Assert.assertEquals("Value not changed", (double) input1[2][0], input0[2][0], 0);
			Assert.assertEquals("Value not changed", (double) input1[3][0], input0[3][0], 0);
			Assert.assertEquals("Value not changed", (double) input1[4][0], input0[4][0], 0);
		}
		//don't copy by reference test
		{
			double[][] input0 = {{0}};

			List output = new ArrayList();

			BaseConverter.sconvert(new ConvertArguments<>(input0, output, Clazz.of(output, Clazz.of(double[].class))));

			Assert.assertNotSame("Copied by reference witch is illegal", input0[0], output.get(0));
			Assert.assertArrayEquals("Not converted right", input0[0], (double[]) output.get(0), 0);
		}
		//recurse test
		{
			Object[] input = {null};
			input[0] = input;

			List output = new ArrayList();

			BaseConverter.sconvert(new ConvertArguments(input, output));

			Assert.assertSame("recursion not converted", output, output.get(0));
		}
	}

	@Test
	public void collection_array() {
		//TODO
	}

	@Test
	public void collection_collection() {
		//TODO
	}

	@Test
	public void collection_list() {
		//TODO
	}

	@Test
	public void map_map() {
		//TODO
	}

	@Test
	public void number__() {
		//TODO
	}

	@Test
	public void object_string() {
		//TODO
	}

	@Test
	public void recurse_object() {
		//TODO
	}

	@Test
	public void string_object() {
		//TODO
	}

	@Test
	public void void_object() {
		//TODO
	}
}
