package com.example.alphapav.lableapplication.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class GetData {

    public static String getFormbodyPostData(String url, HashMap<String, String> paramsMap)
    {
        try {
            URL myUrl = new URL(url);

            //connect
            HttpURLConnection connection = (HttpURLConnection)myUrl.openConnection();

            //setting
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            connection.setDoOutput(true);
            connection.setUseCaches(false);
            //begin connet
            connection.connect();

            StringBuffer params = new StringBuffer();

            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    params.append("&");
                }
                params.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
            outputStreamWriter.write(params.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            int resultCode = connection.getResponseCode();
            System.out.println(resultCode);
            if(resultCode == HttpURLConnection.HTTP_OK) {
                //Success
                StringBuffer buffer = new StringBuffer();
                String line;
                //Get the response
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while((line = responseReader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                responseReader.close();
                String result= TransType.Unicode2String(buffer.toString());
                System.out.print("Get response : " + result);
                System.out.print("Get response : " + buffer.toString());

                return result;

            }
            else {
                System.out.println("Nothing");
            }
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public static String[] WordSplit(String mystr) throws UnsupportedEncodingException {
        String url = "http://api.bosonnlp.com/ner/analysis";

        //String body = JSONArray.fromObject(new String[]{"他太笨了","今天真美好"}).toString();
        //String mystr= "新浪手机讯 4月17日上午消息，近期关于iPhone 6传闻格外多，最新一则来自法国网站nowhereelse.fr，因为iPhone 6屏幕变大，它的电源键将被放在机身侧面。这种推测来源于几张“iPhone 6硅胶壳”图片，“i6”这种写法似乎是中国南方一些附件厂商喜爱的称呼。假设这种保护壳为真，那下一代iPhone最明显的改善就是机身变得更大，并且电源键从机身顶部被转移到了侧面，相信这是为了唤醒手机更方便，现在很多5寸以上大屏手机都采用这种方式。";
        String body = JSONArray.fromObject(new String[]{mystr}).toString();

        //begin request
        try {
            URL myUrl = new URL(url);

            //connect
            HttpURLConnection connection = (HttpURLConnection)myUrl.openConnection();

            //setting
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("X-Token", "8XuxE_SG.31313.9F5vxGVicEJm");
            connection.setRequestProperty("Content-type","application/json");

            //begin connet
            connection.connect();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), "utf-8");	// important
            outputStreamWriter.write(body);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            int resultCode = connection.getResponseCode();
            System.out.println(resultCode);

            if(resultCode == HttpURLConnection.HTTP_OK) {
                //Success
                StringBuffer buffer = new StringBuffer();
                String line;
                //Get the response
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while((line = responseReader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                responseReader.close();
                System.out.print("Get response : " + buffer.toString());
                String[] wordsArr= WordJson2Array(buffer.toString()).clone();
                System.out.print("wordsAr: " + wordsArr);
                return wordsArr;

            }
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }


    public static String[] WordJson2Array(String result)
    {
        //result= "[{\"word\": [\"新浪\", \"手机\", \"讯\", \"4月\", \"17日\", \"上午\", \"消息\", \"，\", \"近期\", \"关于\", \"iPhone\", \" \", \"6\", \"传闻\", \"格外\", \"多\", \"，\", \"最新\", \"一\", \"则\", \"来自\", \"法国\", \"网站\", \"nowhereelse.fr\", \"，\", \"因为\", \"iPhone\", \" \", \"6\", \"屏幕\", \"变\", \"大\", \"，\", \"它\", \"的\", \"电源\", \"键\", \"将\", \"被\", \"放\", \"在\", \"机身\", \"侧面\", \"。\", \"这种\", \"推测\", \"来源于\", \"几\", \"张\", \"“\", \"iPhone\", \" \", \"6\", \"硅胶\", \"壳\", \"”\", \"图片\", \"，\", \"“\", \"i6\", \"”\", \"这种\", \"写法\", \"似乎\", \"是\", \"中国\", \"南方\", \"一些\", \"附件\", \"厂商\", \"喜爱\", \"的\", \"称呼\", \"。\", \"假设\", \"这种\", \"保护壳\", \"为\", \"真\", \"，\", \"那\", \"下一代\", \"iPhone\", \"最\", \"明显\", \"的\", \"改善\", \"就\", \"是\", \"机身\", \"变\", \"得\", \"更\", \"大\", \"，\", \"并且\", \"电源\", \"键\", \"从\", \"机身\", \"顶部\", \"被\", \"转移\", \"到\", \"了\", \"侧面\", \"，\", \"相信\", \"这\", \"是\", \"为了\", \"唤醒\", \"手机\", \"更\", \"方便\", \"，\", \"现在\", \"很多\", \"5\", \"寸\", \"以上\", \"大屏\", \"手机\", \"都\", \"采用\", \"这种\", \"方式\", \"。\"], \"tag\": [\"nz\", \"n\", \"n\", \"t\", \"t\", \"t\", \"n\", \"wd\", \"t\", \"p\", \"nx\", \"w\", \"m\", \"n\", \"d\", \"a\", \"wd\", \"a\", \"m\", \"q\", \"v\", \"ns\", \"n\", \"url\", \"wd\", \"c\", \"nx\", \"w\", \"m\", \"n\", \"v\", \"a\", \"wd\", \"r\", \"ude\", \"n\", \"n\", \"d\", \"pbei\", \"v\", \"p\", \"n\", \"f\", \"wj\", \"r\", \"n\", \"v\", \"m\", \"q\", \"wyz\", \"nx\", \"w\", \"m\", \"n\", \"n\", \"wyy\", \"n\", \"wd\", \"wyz\", \"nx\", \"wyy\", \"r\", \"n\", \"d\", \"vshi\", \"ns\", \"f\", \"m\", \"n\", \"n\", \"v\", \"ude\", \"n\", \"wj\", \"v\", \"r\", \"n\", \"v\", \"a\", \"wd\", \"r\", \"n\", \"nx\", \"d\", \"a\", \"ude\", \"n\", \"d\", \"vshi\", \"n\", \"v\", \"ude\", \"d\", \"a\", \"wd\", \"c\", \"n\", \"n\", \"p\", \"n\", \"f\", \"pbei\", \"v\", \"v\", \"y\", \"f\", \"wd\", \"v\", \"r\", \"vshi\", \"p\", \"v\", \"n\", \"d\", \"a\", \"wd\", \"t\", \"m\", \"m\", \"q\", \"f\", \"n\", \"n\", \"d\", \"v\", \"r\", \"n\", \"wj\"], \"entity\": [[0, 2, \"product_name\"], [3, 6, \"time\"], [10, 13, \"product_name\"], [21, 22, \"location\"], [23, 24, \"product_name\"], [26, 29, \"product_name\"], [50, 54, \"product_name\"], [59, 60, \"product_name\"], [65, 66, \"location\"], [82, 83, \"product_name\"]]}]";
        //result="[{\"word\": [\"在\", \"与\", \"习\", \"主席\", \"的\", \"会谈\", \"中\", \"，\", \"赖斯\", \"大使\", \"重申\", \"美国\", \"致力\", \"于\", \"在\", \"利益\", \"重叠\", \"的\", \"领域\", \"发展\", \"和\", \"深化\", \"务实\", \"合作\", \"，\", \"并\", \"坦率\", \"、\", \"有效\", \"地\", \"处理\", \"分歧\", \"。\", \"By\", \" \", \"U.S.\", \" \", \"Mission\", \" \", \"China\", \"|\", \"29\", \"八月\", \",\", \" \", \"2015\", \"|\", \"主题\", \":\", \"新闻\", \"简报\"], \"tag\": [\"p\", \"p\", \"nr1\", \"n\", \"ude\", \"n\", \"f\", \"wd\", \"nrf\", \"n\", \"v\", \"ns\", \"v\", \"p\", \"p\", \"n\", \"vi\", \"ude\", \"n\", \"v\", \"c\", \"v\", \"vi\", \"v\", \"wd\", \"c\", \"a\", \"wn\", \"a\", \"ude\", \"v\", \"n\", \"wj\", \"nx\", \"w\", \"ns\", \"w\", \"nx\", \"w\", \"nx\", \"w\", \"m\", \"t\", \"wd\", \"w\", \"m\", \"w\", \"n\", \"wm\", \"n\", \"n\"], \"entity\": [[2, 4, \"person_name\"], [8, 9, \"person_name\"], [9, 10, \"job_title\"], [11, 12, \"location\"], [41, 43, \"time\"], [45, 46, \"time\"]]}]\n";
        JSONArray jsonArray = JSONArray.fromObject(result);  //将json字符串转换为JSONArray
        // System.out.print("\n"+jsonArray );
        System.out.print("\n"+jsonArray.size());
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsobj = jsonArray.getJSONObject(i);//把每一个对象转成json对象
            // System.out.print("\njsobj "+jsobj );

            if(jsobj.containsKey("word")) {
                String wordstr = jsobj.getString("word");
                System.out.print("\nwordstr " + wordstr.toString());
                System.out.println("\n flash system out");
                String strArr[] = wordstr.split(",");
                for(int j =0; j<strArr.length;j++)
                {
                    System.out.println(strArr[j]);
                    if(strArr[j].split("\"").length>1)
                    {
                        strArr[j]= strArr[j].split("\"")[1];
                    }
                }
                return strArr;

            }else
            {
                System.out.println("don not exist attribute word");
            }
        }
        return null;
    }

}
