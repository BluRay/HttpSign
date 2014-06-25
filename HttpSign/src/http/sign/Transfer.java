package http.sign;

import java.io.*;

public class Transfer
{

    public Transfer()
    {
    }

    public static int fileToStream(String s, OutputStream outputstream)
        throws Exception
    {
        FileInputStream fileinputstream = new FileInputStream(s);
        int i = 0;
        byte abyte0[] = new byte[bufferSize];
        int j;
        while((j = fileinputstream.read(abyte0)) != -1) 
        {
            outputstream.write(abyte0, 0, j);
            i += j;
        }
        outputstream.flush();
        return i;
    }

    public static int streamToFile(InputStream inputstream, String s)
        throws Exception
    {
        FileOutputStream fileoutputstream = new FileOutputStream(s);
        BufferedInputStream bufferedinputstream = new BufferedInputStream(inputstream, bufferSize);
        int i = 0;
        byte abyte0[] = new byte[bufferSize];
        int j;
        while((j = bufferedinputstream.read(abyte0)) != -1) 
        {
            fileoutputstream.write(abyte0, 0, j);
            i += j;
        }
        fileoutputstream.flush();
        return i;
    }

    private static int bufferSize = 32768;

}


/*
	DECOMPILATION REPORT

	Decompiled from: /home/yangkee/Downloads/Java/Http v3.0/Wrapper.jar
	Total time: 11 ms
	Jad reported messages/errors:
The class file version is 51.0 (only 45.3, 46.0 and 47.0 are supported)
	Exit status: 0
	Caught exceptions:
*/
