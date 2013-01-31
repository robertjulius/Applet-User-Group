package id.co.mik.utility.applet.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;

/*
 * Kelas ini nyontek dari kelas DownloadHttp di aplikasi PP lama.
 * Kelas ini dibuat se-simple mungkin karena tujuannya untuk digunakan oleh applet, jadi size-nya juga tidak terlalu besar.
 */
public class DownloadHelper {
	private URL url;
	private URLConnection urlConnection;
	private HashMap<String, String> param;
	private String host;
	private String port;
	private String contextRoot;
	private String servletPath;
	private String actType;
	private String sessionId;

	public DownloadHelper(String host, String port, String contextRoot,
			String servletPath, String actType, String sessionId)
			throws Exception {

		this.host = host;
		this.port = port;
		this.contextRoot = contextRoot;
		this.servletPath = servletPath;
		this.actType = actType;
		this.sessionId = sessionId;

		param = new HashMap<String, String>();
	}

	public Object doDownload() throws Exception {
		InputStream is = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			url = new URL("http://" + host + ":" + port + "/" + contextRoot
					+ "/" + servletPath + "?actType=" + actType
					+ createParamString() + ";" + sessionId);

			urlConnection = url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setDefaultUseCaches(false);
			urlConnection.setConnectTimeout(5000);

			urlConnection.connect();
			is = urlConnection.getInputStream();
			bis = new BufferedInputStream(is);
			ois = new ObjectInputStream(bis);

			Object object = ois.readObject();
			return object;

		} finally {
			if (ois != null) {
				ois.close();
			}

			if (bis != null) {
				bis.close();
			}

			if (is != null) {
				is.close();
			}
		}
	}

	public HashMap<String, String> getParameter() {
		return param;
	}

	private String createParamString() {
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = param.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			builder.append("&").append(key);
			builder.append("=").append(param.get(key));
		}
		return builder.toString();
	}
}
