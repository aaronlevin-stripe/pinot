/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pinot.segment.local.io.compression;

import java.io.IOException;
import java.nio.ByteBuffer;
import net.jpountz.lz4.LZ4Factory;
import org.apache.pinot.segment.spi.compression.ChunkDecompressor;


/**
 * Implementation of {@link ChunkDecompressor} using LZ4 decompression algorithm.
 * LZ4Factory.fastestInstance().safeDecompressor().decompress(sourceBuffer, destinationBuffer)
 * Compresses the data in buffer 'sourceBuffer' using default compression level
 */
public class LZ4Decompressor implements ChunkDecompressor {

  private static LZ4Factory _lz4Factory;

  public LZ4Decompressor() {
    _lz4Factory = LZ4Factory.fastestInstance();
  }

  @Override
  public int decompress(ByteBuffer compressedInput, ByteBuffer decompressedOutput)
      throws IOException {
    // Safe Decompressor instance is used to avoid data loss
    _lz4Factory.safeDecompressor().decompress(compressedInput, decompressedOutput);
    // When the decompress method returns successfully,
    // dstBuf's position() will be set to its current position() plus the decompressed size of the data.
    // and srcBuf's position() will be set to its limit()
    // Flip operation Make the destination ByteBuffer(decompressedOutput) ready for read by setting the position to 0
    decompressedOutput.flip();
    return decompressedOutput.limit();
  }
}
