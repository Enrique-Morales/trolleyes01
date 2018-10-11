package net.daw.control;
 
//import beans.Json;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.mchange.v2.c3p0.ComboPooledDataSource;



 
/**
 *
 * @author Enrique
 */
public class Login extends HttpServlet {
 
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession();
        Gson gSon = new Gson();
        
        Connection oConnection = null;
        
        String strJson;
        
        
 
        String op = request.getParameter("op");
        String user = request.getParameter("user");
        String pass = "";
        pass = request.getParameter("pass");
        String msg="";
        
        if (op != null) {
        	
        	if (op.equalsIgnoreCase("loggin")) {
        		
        		if (user != null && pass != null) {
                    if (user.equals("Enrique") && pass.equals("contrasenya")) {
                        
                        sesion.setAttribute("sessionvar", user);

                        msg="Sesión iniciada correctamente";
                        
                        respuesta(200,msg,MakePairs(request.getQueryString()),response,request);

                    } else {
                        respuesta(401,"Authentication error",MakePairs(request.getQueryString()),response,request);
                
                    }
                } else {
                    respuesta(401,"No puede dejar ningún campo vacío",MakePairs(request.getQueryString()),response,request);

                }
        		
        	} else if (op.equalsIgnoreCase("connect")) {
        		
//        		try {
//                	Class.forName("com.mysql.jdbc.Driver");
//                } catch (Exception e) {
//                	strJson = "{\"status\":401,\"msg\":\"jdbc driver not found\"}";
//                	out.print(strJson);
//                }
//                
//                try {
//         
//                ComboPooledDataSource cpds = new ComboPooledDataSource();
//                cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver            
//                cpds.setJdbcUrl("jdbc:mysql://localhost:3306/trolleyes");
//                cpds.setUser("root");                                  
//                cpds.setPassword("bitnami"); 
//
//                Connection oConnection = cpds.getConnection();
//                
//                strJson = "{\"status\":200,\"msg\":\"C3P0 Connection OK\"}";
//                out.print(strJson);
//                
//                } catch (Exception e) {
//                	strJson = "{\"status\":401,\"msg\":\"bad C3P0 connection\"}";
//                	out.print(strJson);
//                }
        		
        		Hikari hikari = new Hikari();
        		
        		try {
        			oConnection = hikari.newConnection();
        			strJson = "{\"status\":401,\"msg\":\"connected succesfully\"}";
                                out.print(strJson);
        			
        		} catch (Exception e) {
        			strJson = "{\"status\":401,\"msg\":\"bad connection\"}";
                                out.print(strJson);
        		}
        		
        		
        		
        		if (oConnection != null) {
        			
        			hikari.disposeConnection();
        			
        		}
        		
        		
        		
        		
        		
        	} else if (op.equalsIgnoreCase("logout")) {
        		
        		if(sesion.getAttribute("sessionvar") != null) {
                    sesion.invalidate();
                    respuesta(200,"Sesión cerrada",MakePairs(request.getQueryString()),response,request);

                	} else {
                		respuesta(401,"Aún no estás logeado",MakePairs(request.getQueryString()),response,request);

                	}
        		
        	} else if (op.equalsIgnoreCase("check")) {
        		
        		if (sesion.getAttribute("sessionvar") != null) {
                    respuesta(200,"Usuario loggeado: " + sesion.getAttribute("sessionvar").toString(),MakePairs(request.getQueryString()),response,request);

                } else {
                    respuesta(401,"Aún no estás logeado",MakePairs(request.getQueryString()),response,request);

                }
        		
        	} else if (op.equalsIgnoreCase("secret")) {
        		
        		if (sesion.getAttribute("sessionvar") != null) {
                    respuesta(200,"Si bebes, no conduzcas",MakePairs(request.getQueryString()),response,request);

                } else {
                    respuesta(401,"Logeate para ver el código secreto",MakePairs(request.getQueryString()),response,request);
                }
        		
        	} else {
        		respuesta(401, "Opciones válidas: login/check/secret/logout", MakePairs(request.getQueryString()), response, request);
        	}
        	
            
        } else {
            respuesta(401, "Anyade una opción", null, response, request);
        }
    }
    public void respuesta(Integer status, String msg, Map<String, String> querys,HttpServletResponse response,HttpServletRequest request ) throws IOException {
    	
        Gson gSon = new Gson();
        
        JsonO json = new JsonO();
       
        json.setStatus(status);
        json.setMsg(msg);
        json.setParams(querys);
     
        response.setStatus(status);
        response.getWriter().append(gSon.toJson(json));
    }
 
    //Fuente:
    // https://codereview.stackexchange.com/questions/175332/splitting-url-query-string-to-key-value-pairs
    public static Map<String, String> MakePairs(String input) {
        //ArrayList<String> querys = new ArrayList<>();
        Map<String, String> retVal = new HashMap<String, String>();
        int fromIndex = 0;
        int toIndex = 0;
        while (toIndex != -1) {
            String key = "";
            String value = "";
            toIndex = input.indexOf('=', fromIndex);
            if (toIndex - fromIndex > 1) {
                key = input.substring(fromIndex, toIndex);
                fromIndex = toIndex + 1;
                toIndex = input.indexOf('&', fromIndex);
                if (toIndex == -1) {
                    value = input.substring(fromIndex, input.length());
                } else {
                    value = input.substring(fromIndex, toIndex);
                }
                retVal.put(key, value);
                fromIndex = toIndex + 1;
            } else {
                fromIndex = input.indexOf('&', toIndex) + 1;
            }
        }
        Iterator it = retVal.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry res = (Map.Entry) it.next();
//            querys.add(res.getKey() + " = " + res.getValue());
//        }
        return retVal;
    }
 
   
 
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
 
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
 
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
 
}