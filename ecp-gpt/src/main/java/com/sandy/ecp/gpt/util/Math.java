/*
 * Copyright 2018-2040 the original author or authors.
 *
 * Licensed under the company, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.company.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.gpt.util;

public class Math {

	
	/**
     * Returns as a {@code long} the most significant 64 bits of the 128-bit
     * product of two 64-bit factors.
     *
     * @param x the first value
     * @param y the second value
     * @return the result
     * @see #unsignedMultiplyHigh
     * @since 9
     */
    public static long multiplyHigh(long x, long y) {
        // Use technique from section 8-2 of Henry S. Warren, Jr.,
        // Hacker's Delight (2nd ed.) (Addison Wesley, 2013), 173-174.
        long x1 = x >> 32;
        long x2 = x & 0xFFFFFFFFL;
        long y1 = y >> 32;
        long y2 = y & 0xFFFFFFFFL;

        long z2 = x2 * y2;
        long t = x1 * y2 + (z2 >>> 32);
        long z1 = t & 0xFFFFFFFFL;
        long z0 = t >> 32;
        z1 += x2 * y1;

        return x1 * y1 + z0 + (z1 >> 32);
    }
    
	/**
     * Returns as a {@code long} the most significant 64 bits of the unsigned
     * 128-bit product of two unsigned 64-bit factors.
     *
     * @param x the first value
     * @param y the second value
     * @return the result
     * @see #multiplyHigh
     * @since 18
     */
    public static long unsignedMultiplyHigh(long x, long y) {
        // Compute via multiplyHigh() to leverage the intrinsic
        long result = Math.multiplyHigh(x, y);
        result += (y & (x >> 63)); // equivalent to `if (x < 0) result += y;`
        result += (x & (y >> 63)); // equivalent to `if (y < 0) result += x;`
        return result;
    }
}
