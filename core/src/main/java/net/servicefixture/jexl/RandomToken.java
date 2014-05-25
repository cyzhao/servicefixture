/*
 * Copyright 2006 (C) Chunyun Zhao(Chunyun.Zhao@gmail.com).
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *	http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package net.servicefixture.jexl;

import java.util.Random;

/**
 * @author Chunyun Zhao
 * @since Aug 30, 2006
 */
public class RandomToken {
    public String getString() {
        return Long.toString(System.currentTimeMillis(), Character.MAX_RADIX)
        .toUpperCase();
    }
    
    public String getEmail() {
        return getString() + "@gmail.com";
    }
    
    public int getInt() {
        Random generator = new Random(System.currentTimeMillis());
        return generator.nextInt(10000);
    }
    
    public String digits(int digits) {
        Random generator = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits; i++) {
			sb.append(generator.nextInt(10));
		}
        return sb.toString();
    }
}