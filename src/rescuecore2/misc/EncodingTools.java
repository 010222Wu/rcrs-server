package rescuecore2.misc;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.nio.charset.Charset;

/**
   A bunch of useful tools for encoding and decoding things like integers.
 */
public final class EncodingTools {
    /** The size of an INT_32 in bytes. */
    public static final int INT_32_SIZE = 4;

    /** Charset for encoding/decoding strings. Should always be UTF-8 */
    private static final Charset CHARSET = Charset.forName("UTF-8");

    /**
       Private constructor: this is a utility class.
    */
    private EncodingTools() {}

    /** Turn off the checkstyle test for magic numbers since we use a lot of them here */
    /** CHECKSTYLE:OFF:MagicNumber */

    /**
       Write a 32-bit integer to an OutputStream, big-endian style.
       @param i The integer to write.
       @param out The OutputStream to write it to.
       @throws IOException If the OutputStream blows up.
     */
    public static void writeInt32(int i, OutputStream out) throws IOException {
        // Most significant byte first
        out.write((byte) (i >> 24) & 0xFF);
        out.write((byte) (i >> 16) & 0xFF);
        out.write((byte) (i >> 8) & 0xFF);
        out.write((byte) i & 0xFF);
    }

    /**
       Write a 32-bit integer to a byte array, big-endian style.
       @param i The integer to write.
       @param out The buffer to write it to.
       @param offset Where in the buffer to write it.
     */
    public static void writeInt32(int i, byte[] out, int offset) {
        // Most significant byte first
        out[offset] = (byte) ((i >> 24) & 0xFF);
        out[offset + 1] = (byte) ((i >> 16) & 0xFF);
        out[offset + 2] = (byte) ((i >> 8) & 0xFF);
        out[offset + 3] = (byte) (i & 0xFF);
    }

    /**
       Read a 32-bit integer from an input stream, big-endian style.
       @param in The InputStream to read from.
       @return The next big-endian, 32-bit integer in the stream.
       @throws IOException If the InputStream blows up.
       @throws EOFException If the end of the stream is reached.
     */
    public static int readInt32(InputStream in) throws IOException {
        int first = in.read();
        if (first == -1) {
            throw new EOFException("Broken input pipe. Read 0 bytes of 4.");
        }
        int second = in.read();
        if (second == -1) {
            throw new EOFException("Broken input pipe. Read 1 bytes of 4.");
        }
        int third = in.read();
        if (third == -1) {
            throw new EOFException("Broken input pipe. Read 2 bytes of 4.");
        }
        int fourth = in.read();
        if (fourth == -1) {
            throw new EOFException("Broken input pipe. Read 3 bytes of 4.");
        }
        return (first << 24) | (second << 16) | (third << 8) | fourth;
    }

    /**
       Read a 32-bit integer from an input stream, little-endian style.
       @param in The InputStream to read from.
       @return The next little-endian, 32-bit integer in the stream.
       @throws IOException If the InputStream blows up.
       @throws EOFException If the end of the stream is reached.
     */
    public static int readInt32LE(InputStream in) throws IOException {
        int first = in.read();
        if (first == -1) {
            throw new EOFException("Broken input pipe. Read 0 bytes of 4.");
        }
        int second = in.read();
        if (second == -1) {
            throw new EOFException("Broken input pipe. Read 1 bytes of 4.");
        }
        int third = in.read();
        if (third == -1) {
            throw new EOFException("Broken input pipe. Read 2 bytes of 4.");
        }
        int fourth = in.read();
        if (fourth == -1) {
            throw new EOFException("Broken input pipe. Read 3 bytes of 4.");
        }
        return (fourth << 24) | (third << 16) | (second << 8) | first;
    }

    /**
       Read a 32-bit integer from a byte array, big-endian style.
       @param in The buffer to read from.
       @param offset Where to begin reading.
       @return The next big-endian, 32-bit integer in the buffer.
     */
    public static int readInt32(byte[] in, int offset) {
        return (in[offset] << 24) | (in[offset + 1] << 16) | (in[offset + 2] << 8) | (in[offset + 3]);
    }

    /**
       Read a 32-bit integer from a byte array, big-endian style. This is equivalent to calling {@link #readInt32(byte[], int) readInt32(in, 0)}.
       @param in The buffer to read from.
       @return The first big-endian, 32-bit integer in the buffer.
     */
    public static int readInt32(byte[] in) {
        return readInt32(in, 0);
    }

    /**
       Write a String to an OutputStream. Strings are always in UTF-8.
       @param s The String to write.
       @param out The OutputStream to write to.
       @throws IOException If the OutputStream blows up.
     */
    public static void writeString(String s, OutputStream out) throws IOException {
        byte[] bytes = s.getBytes(CHARSET);
        writeInt32(bytes.length, out);
        out.write(bytes);
    }

    /**
       Write a String to a byte array. Strings are always in UTF-8.
       @param s The String to write.
       @param out The byte array to write to. Make sure it's big enough!
       @param offset The index to start writing from.
     */
    public static void writeString(String s, byte[] out, int offset) {
        byte[] bytes = s.getBytes(CHARSET);
        writeInt32(bytes.length, out, offset);
        System.arraycopy(bytes, 0, out, offset + 4, bytes.length);
    }

    /**
       Read a String from an InputStream.
       @param in The InputStream to read.
       @throws IOException If the InputStream blows up.
       @throws EOFException If the end of the stream is reached.
    */
    public static String readString(InputStream in) throws IOException {
        int length = readInt32(in);
        byte[] buffer = new byte[length];
        int count = 0;
        while (count < length) {
            int read = in.read(buffer, count, length - count);
            if (read == -1) {
                throw new EOFException("Broken input pipe. Read " + count + " bytes of " + length + ".");
            }
            count += read;
        }
        return new String(buffer, CHARSET);
    }

    /**
       Read a String from a byte array. This is equivalent to calling {@link #readString(byte[], int) readString(in, 0)}.
       @param in The byte array to read.
    */
    public static String readString(byte[] in) {
        return readString(in, 0);
    }

    /**
       Read a String from a byte array.
       @param in The byte array to read.
       @param offset The index in the array to read from.
    */
    public static String readString(byte[] in, int offset) {
        int length = readInt32(in, offset);
        byte[] buffer = new byte[length];
        System.arraycopy(in, offset + 4, buffer, 0, length);
        return new String(buffer, CHARSET);
    }

    /**
       Read a fixed number of bytes from an InputStream into an array.
       @param size The number of bytes to read.
       @param in The InputStream to read from.
       @return A new byte array containing the bytes.
       @throws IOException If the read operation fails.
    */
    public static byte[] readBytes(int size, InputStream in) throws IOException {
        byte[] buffer = new byte[size];
        int total = 0;
        while (total < size) {
            int read = in.read(buffer, total, size - total);
            if (read == -1) {
                throw new EOFException("Broken input pipe. Read " + total + " bytes of " + size + ".");
            }
            total += read;
        }
        return buffer;
    }

    /**
       Write a double to an OutputStream.
       @param d The double to write.
       @param out The OutputStream to write it to.
       @throws IOException If the OutputStream blows up.
     */
    public static void writeDouble(double d, OutputStream out) throws IOException {
        long bits = Double.doubleToLongBits(d);
        out.write((byte) (bits >> 56) & 0xFF);
        out.write((byte) (bits >> 48) & 0xFF);
        out.write((byte) (bits >> 40) & 0xFF);
        out.write((byte) (bits >> 32) & 0xFF);
        out.write((byte) (bits >> 24) & 0xFF);
        out.write((byte) (bits >> 16) & 0xFF);
        out.write((byte) (bits >> 8) & 0xFF);
        out.write((byte) bits & 0xFF);
    }

    /**
       Write a double integer to a byte array.
       @param d The double to write.
       @param out The buffer to write it to.
       @param offset Where in the buffer to write it.
     */
    public static void writeDouble(double d, byte[] out, int offset) {
        long bits = Double.doubleToLongBits(d);
        out[offset + 0] = (byte) ((bits >> 56) & 0xFF);
        out[offset + 1] = (byte) ((bits >> 48) & 0xFF);
        out[offset + 2] = (byte) ((bits >> 40) & 0xFF);
        out[offset + 3] = (byte) ((bits >> 32) & 0xFF);
        out[offset + 4] = (byte) ((bits >> 24) & 0xFF);
        out[offset + 5] = (byte) ((bits >> 16) & 0xFF);
        out[offset + 6] = (byte) ((bits >> 8) & 0xFF);
        out[offset + 7] = (byte) (bits & 0xFF);
    }

    /**
       Read a double from an input stream.
       @param in The InputStream to read from.
       @return The next double in the stream.
       @throws IOException If the InputStream blows up.
       @throws EOFException If the end of the stream is reached.
     */
    public static double readDouble(InputStream in) throws IOException {
        long[] data = new long[8];
        for (int i = 0; i < data.length; ++i ) {
            data[i] = in.read();
            if (data[i] == -1) {
                throw new EOFException("Broken input pipe. Read " + i + " bytes of 8.");
            }
        }
        long result = data[0] << 56
            | data[1] << 48
            | data[2] << 40
            | data[3] << 32
            | data[4] << 24
            | data[5] << 16
            | data[6] << 8
            | data[7];
        return Double.longBitsToDouble(result);
    }

    /**
       Read a double from a byte array.
       @param in The buffer to read from.
       @param offset Where to begin reading.
       @return The next double in the buffer.
     */
    public static double readDouble(byte[] in, int offset) {
        long[] parts = new long[8];
        for (int i = 0; i < 8; ++i) {
            parts[i] = in[offset + i];
        }
        long result = parts[0] << 56
            | parts[1] << 48
            | parts[2] << 40
            | parts[3] << 32
            | parts[4] << 24
            | parts[5] << 16
            | parts[6] << 8
            | parts[7];
        return Double.longBitsToDouble(result);
    }

    /**
       Read a double from a byte array. This is equivalent to calling {@link #readDouble(byte[], int) readDouble(in, 0)}.
       @param in The buffer to read from.
       @return The first double in the buffer.
     */
    public static double readDouble(byte[] in) {
        return readDouble(in, 0);
    }

    /**
       Call InputStream.skip until exactly <code>count</code> bytes have been skipped. If InputStream.skip ever returns a negative number then an EOFException is thrown.
       @param in The InputStream to skip.
       @param count The number of bytes to skip.
       @throws IOException If the bytes cannot be skipped for some reason.
     */
    public static void reallySkip(InputStream in, long count) throws IOException {
        long done = 0;
        while (done < count) {
            long next = in.skip(count - done);
            if (next < 0) {
                throw new EOFException();
            }
            done += next;
        }
    }

    /** CHECKSTYLE:ON:MagicNumber */
}