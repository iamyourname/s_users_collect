package org.example.s_users_collect;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class ZabbixAPI {

    final static Logger logger = LoggerFactory.getLogger(ZabbixAPI.class);
    public static String jsonToken = "{" +
            "\"jsonrpc\":\"2.0\"," +
            "\"method\":\"user.login\"," +
            "\"params\":" +
            "{\"user\":\"mivyamoiseev\"," +
            "\"password\":\"xid123MTPP_\"}," +
            "\"id\":1" +
            "}";
    public static String token;

    public static String zabbixSenderAPI(String body) throws IOException {
        String url = "https://zabbix.vimpelcom.ru/api_jsonrpc.php";
        //logger.info(body);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();
        //https://code-with-me.jetbrains.com.cn/Dj540epCQVhviRMByy4GfA#p=IU&fp=DD6B74042F9C965451EA4FDF1096193F149115560A937603923899706CDF3971&newUi=true
        int responseCode = con.getResponseCode();
        //System.out.println("Response Code: " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //System.out.println(response.toString());
        if(responseCode==200){
           // logger.info(response.toString());
            JSONObject responseJson = new JSONObject(response.toString());

            JSONObject responseLast = new JSONObject(responseJson
                    .get("result")
                    .toString()
                    .replace("[{","{")
                    .replace("}]","}"));
            return responseLast.get("lastvalue").toString();

        }else {
            return Integer.toString(responseCode);
        }

    }

    public static void updateAuthToken() throws IOException {
        String url = "https://zabbix.vimpelcom.ru/api_jsonrpc.php";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonToken);
        wr.flush();
        wr.close();
        //https://code-with-me.jetbrains.com.cn/Dj540epCQVhviRMByy4GfA#p=IU&fp=DD6B74042F9C965451EA4FDF1096193F149115560A937603923899706CDF3971&newUi=true
        int responseCode = con.getResponseCode();
        //System.out.println("Response Code: " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject jsonResp = new JSONObject(response.toString());
        //System.out.println(response.toString());

        token= jsonResp.getString("result");
        logger.info("token has been updated " + token);
    }



    public static String getUsersCount() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"28317570\"\n" +
                "    },\n" +
                "    \"hostids\": \"103390\"\n" +
                "  }\n" +
                "}";

        return zabbixSenderAPI(apiUsersCount);
    }

    public static String getUniqueUsersFromDay() throws IOException {

        String getUniqueUsersFromDay = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"38256763\"\n" +
                "    },\n" +
                "    \"hostids\": \"138371\"\n" +
                "  }\n" +
                "}";

        JSONObject guufd = new JSONObject(zabbixSenderAPI(getUniqueUsersFromDay));
        return guufd.getString("count");

    }


    public static String getActiveIncToday() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"31283192\"\n" +
                "    },\n" +
                "    \"hostids\": \"108707\"\n" +
                "  }\n" +
                "}";

        return "ОТКРЫТЫХ ИНЦИДЕНТОВ: "+zabbixSenderAPI(apiUsersCount);
    }

    public static String getDoneIncToday() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"31282913\"\n" +
                "    },\n" +
                "    \"hostids\": \"108707\"\n" +
                "  }\n" +
                "}";

        return "РЕШЕНО ЗА СЕГОДНЯ: "+zabbixSenderAPI(apiUsersCount);
    }

    public static String getIncomingIncToday() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"31126668\"\n" +
                "    },\n" +
                "    \"hostids\": \"108707\"\n" +
                "  }\n" +
                "}";

        return "ПРИШЛО ЗА СЕГОДНЯ: "+zabbixSenderAPI(apiUsersCount);
    }

    public static String getWaitIncToday() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"31283468\"\n" +
                "    },\n" +
                "    \"hostids\": \"108707\"\n" +
                "  }\n" +
                "}";

        return "В ОЖИДАНИИ: "+zabbixSenderAPI(apiUsersCount);
    }

    public static String getAllInc() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"32674559\"\n" +
                "    },\n" +
                "    \"hostids\": \"108707\"\n" +
                "  }\n" +
                "}";

        return "ВСЕГО ИНЦИДЕНТОВ: "+zabbixSenderAPI(apiUsersCount);
    }

    public static String getInWorkInc() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"32910967\"\n" +
                "    },\n" +
                "    \"hostids\": \"108707\"\n" +
                "  }\n" +
                "}";

        return "ИНЦИДЕНТОВ В РАБОТЕ: "+zabbixSenderAPI(apiUsersCount);
    }


    public static String getProcErrors() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"30378713\"\n" +
                "    },\n" +
                "    \"hostids\": \"104447\"\n" +
                "  }\n" +
                "}";

        return "Процент ошибочных операций: "+zabbixSenderAPI(apiUsersCount);
    }

    //28986977

    public static String getUniqueUsersFromWeek() throws IOException {

        String getUniqueUsersFromWeek = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"38257177\"\n" +
                "    },\n" +
                "    \"hostids\": \"138371\"\n" +
                "  }\n" +
                "}";

        JSONObject guufw = new JSONObject(zabbixSenderAPI(getUniqueUsersFromWeek));
        return guufw.getString("count");

    }
    public static String getUniqueUsersFromMonth() throws IOException {

        String getUniqueUsersFromMonth = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"38259468\"\n" +
                "    },\n" +
                "    \"hostids\": \"138371\"\n" +
                "  }\n" +
                "}";

        JSONObject guufm = new JSONObject(zabbixSenderAPI(getUniqueUsersFromMonth));
        return guufm.getString("count")+"!"+guufm.getString("MONTH_NAME");

    }
    public static String getSuccessActions() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"28986977\"\n" +
                "    },\n" +
                "    \"hostids\": \"104447\"\n" +
                "  }\n" +
                "}";

        return "Успешных операций: "+zabbixSenderAPI(apiUsersCount);
    }

    //30378766
    public static String getUniqueUsersFromYear() throws IOException {

        String getUniqueUsersFromYear = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"38571465\"\n" +
                "    },\n" +
                "    \"hostids\": \"138371\"\n" +
                "  }\n" +
                "}";

        JSONObject guufy = new JSONObject(zabbixSenderAPI(getUniqueUsersFromYear));
        return guufy.getString("count");

    }
    public static String getProcErrors_K() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"30378766\"\n" +
                "    },\n" +
                "    \"hostids\": \"104447\"\n" +
                "  }\n" +
                "}";

        return "PROC_ERRORS_K: "+zabbixSenderAPI(apiUsersCount);
    }

    //103486&itemid=28336322

    public static String getZakupkiStatus() throws IOException {
        String apiUsersCount = "{\n" +
                "  \"method\": \"item.get\",\n" +
                "  \"auth\": \""+token+"\",\n" +
                "  \"id\": 1,\n" +
                "  \"jsonrpc\": \"2.0\",\n" +
                "  \"params\": {\n" +
                "    \"filter\": {\n" +
                "      \"itemid\": \"28336322\"\n" +
                "    },\n" +
                "    \"hostids\": \"103486\"\n" +
                "  }\n" +
                "}";

       // if(zabbixSenderAPI(apiUsersCount).equals("401"))
        //    return "ДОСТУПНОСТЬ СТРАНИЦЫ: "+"OK";

        return "Доступность страницы zakupki.beeline.ru: OK";//+zabbixSenderAPI(apiUsersCount)
    }

    public static String getZabbixInfo() throws IOException, SQLException, ClassNotFoundException {

        StringBuilder resultMes= new StringBuilder();

        logger.info("START COLLECT ZABBIX");
       // LogService.insertLog("LOADING","ZABBIX_COLLECT","","INFO","");
        updateAuthToken();
        //resultMes.append(getUsersCount());
        resultMes.append("<hr><h3>СОСТОЯНИЕ СИСТЕМЫ БАЗИС</h3>");
        resultMes.append("  <font color=\"green\" style=\"bold\">СОСТОЯНИЕ СИСТЕМЫ: OK</font>" +"<br>");
        resultMes.append("  "+getProcErrors() +"<br>");
        resultMes.append("  "+getSuccessActions() +"   <br>");
        resultMes.append(getZakupkiStatus()+"   <br>");
        resultMes.append(getUsersCount()+"   <br><br>");
        //resultMes.append(getProcErrors());
        resultMes.append("<hr><h3>СТАТИСТИКА ИНЦИДЕНТОВ</h3>");
        resultMes.append(getAllInc()+"<br>");
        resultMes.append(getIncomingIncToday()+"<br>");
        resultMes.append(getDoneIncToday()+"<br>");
        resultMes.append(getInWorkInc()+"<br>");
        //resultMes.append(getActiveIncToday()+"<br>");
        resultMes.append(getWaitIncToday()+"<hr>");
        //LogService.insertLog("DONE","ZABBIX_COLLECT","","INFO","");

        return resultMes.toString();

    }

}
