/*
 * Copyright 2005-2006 (C) Chunyun Zhao. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Due credit should be given to Chunyun Zhao. (Chunyun.Zhao@gmail.com)
 *
 * THIS SOFTWARE IS PROVIDED BY CHUNYUN ZHAO AND OTHER CONTRIBUTORS ''AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL CHUNYUN ZHAO OR OTHER CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.servicefixture.parser;

import junit.framework.TestCase;

public class CollectionBuilderCloneTest extends TestCase {
	public void testCloneArrayBuilder() {
		ArrayBuilder builder = new ArrayBuilder();
		Object clone = builder.clone();
		assertEquals(ArrayBuilder.class, clone.getClass());
	}
	
	public void testCloneListBuilder() {
		ListBuilder builder = new ListBuilder();
		Object clone = builder.clone();
		assertEquals(ListBuilder.class, clone.getClass());
	}
}
