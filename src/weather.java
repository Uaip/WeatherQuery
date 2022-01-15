import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class weather implements Runnable {
    public static void main(String[] args) {
        new Thread(new weather()).start();
    }

    @Override
    public void run() {
        dataget();
    }

    void dataget() {
        String city = "temp";
        while (!city.equalsIgnoreCase("exit")) {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入你要查询的城市:");
            city = sc.nextLine();
            int cityCode = new sqlsearch(city).get_adcode();
            System.out.println(cityCode);
            String urlstr = "https://restapi.amap.com/v3/weather/weatherInfo?key=a466f1a0314f4165b118b1f8fe887678&city="
                    + String.valueOf(cityCode) + "&extensions=base&output=xml";
            // System.out.println(urlstr);
            try {
                // 实例化URL对象
                URL url = new URL(urlstr);
                // 通过URL对象打开一个连接,显示转换为httpURLConnection类
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 设置连接方式：get
                connection.setRequestMethod("GET");
                // 设置连接远程服务的超时时间
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(60000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    InputStream stream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                    StringBuilder res = new StringBuilder();
                    String line = reader.readLine();
                    res.append(line);
                    while ((line = reader.readLine()) != null) {
                        res.append(line);
                    }
                    dataprocess(res.toString());
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void dataprocess(String data) {
        // System.out.println(data);
        try {
            int left = data.indexOf("<status>") + "<status>".length();
            int right = data.lastIndexOf("</status>");
            // System.out.println(data.substring(left, right));
            if (data.substring(left, right).equals("1")) {
                // 城市
                left = data.indexOf("<city>") + "<city>".length();
                right = data.lastIndexOf("</city>");
                System.out.print(data.substring(left, right) + "\t");

                // 天气
                left = data.indexOf("<weather>") + "<weather>".length();
                right = data.lastIndexOf("</weather>");
                System.out.print("天气:" + data.substring(left, right) + "\t");

                // 温度
                left = data.indexOf("<temperature>") + "<temperature>".length();
                right = data.lastIndexOf("</temperature>");
                System.out.print("温度:" + data.substring(left, right) + "℃" + "\t\t");

                // 风向
                left = data.indexOf("<winddirection>") + "<winddirection>".length();
                right = data.lastIndexOf("</winddirection>");
                System.out.print(data.substring(left, right) + "风" + "\t");

                // 风力
                left = data.indexOf("<windpower>") + "<windpower>".length();
                right = data.lastIndexOf("</windpower>");
                System.out.print("风力 " + data.substring(left, right) + " 级" + "\t");

                // 湿度
                left = data.indexOf("<humidity>") + "<humidity>".length();
                right = data.lastIndexOf("</humidity>");
                System.out.print("湿度:" + data.substring(left, right) + "\t\t");

                // 时间
                left = data.indexOf("<reporttime>") + "<reporttime>".length();
                right = data.lastIndexOf("</reporttime>");
                System.out.println("时间:" + data.substring(left, right) + "\t");

            } else {
                System.out.println("查询失败");
            }
        } catch (Exception e) {
            System.out.println("查询失败");
        }

    }
}