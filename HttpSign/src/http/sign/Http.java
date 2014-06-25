package http.sign;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

// Referenced classes of package wrapper:
//            Transfer

public class Http
{

    public Http(String s, String s1)
    {
        urlString = null;
        timeOut = 10000;
        headerDatasMap = new HashMap();
        postDatasList = new ArrayList();
        uploadFilesList = new ArrayList();
        urlString = s;
        charset = s1;
        setHeader("Accept-Language", "zh-CN;q=1, zh;q=1, en-us;q=0.5, en;q=0.3");
        setHeader("Accept-Encoding", "identity");
        setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
    }

    public int request()
        throws Exception
    {
        createRequest();
        if(!uploadFilesList.isEmpty())
        {
            String s = "\r\n";
            String s1 = "--";
            String s2 = String.valueOf((new Random()).nextInt()).substring(2);
            connection.setRequestProperty("Content-Type", (new StringBuilder()).append("multipart/form-data; boundary=").append(s2).toString());
            try
            {
                connection.connect();
            }
            catch(Exception exception2)
            {
                return -1;
            }
            OutputStream outputstream = connection.getOutputStream();
            for(Iterator iterator = uploadFilesList.iterator(); iterator.hasNext(); outputstream.write(s.getBytes(charset)))
            {
                String as[] = (String[])iterator.next();
                outputstream.write((new StringBuilder()).append(s1).append(s2).append(s).append("Content-Disposition: form-data; name=\"").append(as[0]).append("\"; filename=\"").append(as[2]).append("\"").append(s).append("Content-Type: application/octet-stream").append(s).append(s).toString().getBytes(charset));
                Transfer.fileToStream(as[1], outputstream);
            }

            String as1[];
            for(Iterator iterator1 = postDatasList.iterator(); iterator1.hasNext(); outputstream.write((new StringBuilder()).append(s1).append(s2).append(s).append("Content-Disposition: form-data; name=\"").append(as1[0]).append("\"").append(s).append("Content-Type: text/plain;charset=").append(charset).append(s).append(s).append(as1[1]).append(s).toString().getBytes(charset)))
                as1 = (String[])iterator1.next();

            outputstream.write((new StringBuilder()).append(s1).append(s2).append(s1).toString().getBytes(charset));
        } else
        if(!postDatasList.isEmpty())
        {
            try
            {
                connection.connect();
            }
            catch(Exception exception)
            {
                return -1;
            }
            connection.getOutputStream().write(createPostData(postDatasList).getBytes(charset));
        } else
        {
            try
            {
                connection.connect();
            }
            catch(Exception exception1)
            {
                return -1;
            }
        }
        connection.getHeaderFields();
        return 0;
    }

    private void createRequest()
        throws Exception
    {
        URL url = new URL(urlString);
        connection = (HttpURLConnection)url.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setUseCaches(false);
        connection.setConnectTimeout(timeOut);
        connection.setReadTimeout(timeOut);
        connection.setDoInput(true);
        if(!uploadFilesList.isEmpty())
        {
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", charset);
            connection.setDoOutput(true);
        } else
        if(!postDatasList.isEmpty())
        {
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", (new StringBuilder()).append("application/x-www-form-urlencoded;charset=").append(charset).toString());
            connection.setDoOutput(true);
        } else
        {
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", (new StringBuilder()).append("text/html;charset=").append(charset).toString());
        }
        prepareHeaders();
    }

    public InputStream getInputStream()
        throws Exception
    {
        return connection.getInputStream();
    }

    public Map getHeaders()
    {
        return connection.getHeaderFields();
    }

    public String getHeader(String s)
    {
        return connection.getHeaderField(s);
    }

    public void setHeader(String s, String s1)
    {
        headerDatasMap.put(s, s1);
    }

    public void setTimeOut(int i)
    {
        timeOut = i;
    }

    public void addPostData(String s, String s1)
    {
        String as[] = {
            s, s1
        };
        postDatasList.add(as);
    }

    public void addUploadFile(String s, String s1, String s2)
        throws Exception
    {
        String as[] = {
            s, s1, s2
        };
        uploadFilesList.add(as);
    }

    private void prepareHeaders()
    {
        String s;
        String s1;
        for(Iterator iterator = headerDatasMap.entrySet().iterator(); iterator.hasNext(); connection.setRequestProperty(s, s1))
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            s = (String)entry.getKey();
            s1 = (String)entry.getValue();
        }

    }

    private String createPostData(List list)
        throws Exception
    {
        StringBuilder stringbuilder = new StringBuilder();
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            String as[] = (String[])iterator.next();
            String s = URLEncoder.encode(as[0], charset);
            String s1 = URLEncoder.encode(as[1], charset);
            if(stringbuilder.length() < 1)
                stringbuilder.append(s).append("=").append(s1);
            else
                stringbuilder.append("&").append(s).append("=").append(s1);
        }

        return stringbuilder.toString();
    }

    private String urlString;
    private String charset;
    private int timeOut;
    private HashMap headerDatasMap;
    private List postDatasList;
    private List uploadFilesList;
    private HttpURLConnection connection;

    static 
    {
        if(CookieHandler.getDefault() == null)
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }
}


/*
	DECOMPILATION REPORT

	Decompiled from: /home/yangkee/Downloads/Java/Http v3.0/Wrapper.jar
	Total time: 6 ms
	Jad reported messages/errors:
The class file version is 51.0 (only 45.3, 46.0 and 47.0 are supported)
	Exit status: 0
	Caught exceptions:
*/
