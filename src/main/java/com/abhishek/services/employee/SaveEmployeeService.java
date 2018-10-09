package com.abhishek.services.employee;

import com.abhishek.core.HibernateUtil;
import com.abhishek.core.entities.Employee;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class SaveEmployeeService implements RequestStreamHandler {

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        JSONObject responseJson = new JSONObject();
        SaveEmployeeRequest request = null;
        Gson gson = new Gson();
        try {
            JSONObject event = (JSONObject) parser.parse(reader);
            System.out.println("Event  :"+event.toJSONString());
            String body  = (String)event.get("body");

            request =  gson.fromJson(body, SaveEmployeeRequest.class);
        } catch (ParseException pex) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex);
        }

        Employee employee = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            employee = new Employee();
            employee.setName(request.getName());
            employee.setDesignation(request.getDesignation());
            session.save(employee);
            session.getTransaction().commit();

            context.getLogger().log("Employee Id :"+employee.getId());
        }


        JSONObject responseBody = new JSONObject();
        responseBody.put("id", employee.getId());

        JSONObject headerJson = new JSONObject();
        headerJson.put("custom-header", "Custom Header Value");

        responseJson.put("statusCode", 200);
        responseJson.put("headers", headerJson);
        responseJson.put("body", responseBody.toString());

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toJSONString());

        System.out.println("Response :"+responseJson.toJSONString());
        writer.close();

    }
}
