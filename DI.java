package com.adi;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DI {
    protected Integer resourceId;
    protected Context context;
    protected JSONObject jsonObject;

    public DI(Integer resourceId, Context context) {
        this.resourceId = resourceId;
        this.context = context;

        try {
            this.jsonObject = new JSONObject(getJSONFileContents());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected String getJSONFileContents() {
        InputStream is = this.context.getResources().openRawResource(this.resourceId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;
        StringBuilder builder = new StringBuilder();

        try {
            // While the BufferedReader readLine is not null
            while ((readLine = br.readLine()) != null) {
                builder.append(readLine);
            }

            // Close the InputStream and BufferedReader
            is.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public <Any> Any getDependency(String methodName, String from) {
        if (from.equals("context")) {
            try {
                Method method = this.context.getClass().getMethod(methodName);

                try {
                    return (Any) method.invoke(this.context);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    protected <Any> Object[] processDependencies(JSONArray dependencies) {
        Object[] arguments = new Object[1];

        for (int i = 0; i < dependencies.length(); i++) {
            JSONObject subObj = null;

            try {
                subObj = dependencies.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (subObj != null) {
                if (subObj.has("service")) {
                    try {
                        arguments[0] = (Any) this.getService(subObj.getString("service"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        arguments[0] = (Any) this.getDependency(subObj.getString("method"), subObj.getString("from"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return arguments;
    }

    public <Any> Any getService(String serviceName) {
        try {
            JSONObject subObj = jsonObject.getJSONObject(serviceName);
            Class c = Class.forName(subObj.getString("fqns"));
            Object[] arguments = new Object[0];

            if (subObj.has("dependencies")) {
                arguments = this.processDependencies(subObj.getJSONArray("dependencies"));
            }

            Constructor constructor = null;
            constructor = c.getConstructors()[0];

            try {
                return (Any) constructor.newInstance(arguments);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
