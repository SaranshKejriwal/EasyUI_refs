

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import org.apache.commons.io.FileUtils;


//import java.util.ArrayList;
//import java.util.Random;
//import javax.servlet.RequestDispatcher;
//import android.util.Base64;
//import org.apache.commons.codec.binary.*;


/**
 * Servlet implementation class ReadAjaxParameters
 */
@WebServlet("/dyna")
public class DynaDGServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DynaDGServlet() {
		super();
		
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Reached Dyna doGet method");
		/*pageNum=Integer.parseInt(request.getParameter("pageNum"));
		pageSize=Integer.parseInt(request.getParameter("pageSize"));
		System.out.println("Page Number Dyna:"+pageNum);
		System.out.println("Page Size:"+pageSize);
		
		
		response.sendRedirect("webpages/ProgressBarTest.html?Count=763&pageSiz="+pageSize+"&pageNum="+pageNum);*/
		doPost(request,response);
       
	}

	
	
	
	
	
	
	
	public PrintWriter makeDynamicDG(HttpServletRequest request,HttpServletResponse response,String exportType)
	{
			
		//String FetchQ2="select u.geid, u.userid, u.name_first, u.name_last,u.sip_location,u.sip_unit, d.group_name,u.status from cif_prod_owner.ecif_user_group d, cif_prod_owner.ecif_user u,CIF_PROD_OWNER.ECIF_USER_GROUP_SYSTEM c WHERE u.USERID = c.USERID AND u.STATUS = c.STATUS AND c.GROUP_ID = d.GROUP_ID and rownum< 10 ";
		String FetchQ2="select * from cif_prod_owner.ecif_users where rownum<15";
		
		//This can be any query that we want
		
		
		try 
		{
			Connection conn=ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();
			
			ResultSet rset = stmt.executeQuery(FetchQ2);
			
			System.out.println("Query Executed in Dyna");
			/*This Query is not just any random statement. There must be a list of such SQ's */
			int proxyBeanSize=0;
			ResultSetMetaData rsmd = rset.getMetaData();
			proxyBeanSize = rsmd.getColumnCount();
			
			System.out.println("Bean Size="+proxyBeanSize);
			String[] proxyKeyBean=new String[proxyBeanSize];//holds keys for JSON
			ArrayList<String[]> proxyValueList=new ArrayList<String[]>();//holds value array for JSON
			ArrayList<String> jsonObj=new ArrayList<String>();
			ArrayList<String> xmlObj=new ArrayList<String>();
			
			String txtHead="";
			String txtBody="";
			for(int u=1;u<=proxyBeanSize;u++)//indexing starts with 1 here
			{
				proxyKeyBean[u-1]=rsmd.getColumnName(u);//add 1st column name to 0th index
				txtHead=txtHead+rsmd.getColumnName(u)+"\t";
				//System.out.println(proxyKeyBean[u-1]);
			}
			//System.out.println("txtHead= "+txtHead);
			int recCount=0;
			if(!rset.next())
			{
				System.out.println("No Result Set");
			}
			while(rset.next())
			{
				recCount++;
				String[] tempValueBean=new String[proxyBeanSize];
				String tempJson="{";
				for(int i=0;i<proxyBeanSize;i++)
				{
					tempValueBean[i]=rset.getString(i+1);//getString() index starts from 1
					tempJson=tempJson+"\""+proxyKeyBean[i]+"\":"+"\""+tempValueBean[i]+"\"";
					txtBody=txtBody+tempValueBean[i]+"\t";
					if(i!=proxyBeanSize-1)
					{
						tempJson=tempJson+",";
					}
				}
				txtBody=txtBody+"\n";//newline when 1 resultset ends
				tempJson=tempJson.replaceAll("null"," ")+"}";
				//System.out.println("Json:: "+tempJson);
				//System.out.println("xml:: "+XML.toString(new JSONObject (tempJson)));//printing tempXML
				//System.out.println("");
				xmlObj.add(XML.toString(new JSONObject (tempJson)));//create arraylist of xml
								
				jsonObj.add(tempJson);
				proxyValueList.add(tempValueBean);
			}
			//System.out.println("txtBody="+txtBody);
			String resultjsonString="";
			String resultxmlString="";
			
			for(int i=0;i<recCount;i++)
			{
						resultjsonString=resultjsonString+""+jsonObj.get(i)+"";
						resultxmlString=resultxmlString+"<row>"+xmlObj.get(i)+"</row>";
						if(i!=recCount-1)
						{
							resultjsonString=resultjsonString+",";
						}
			}
			
			
			
			
			resultjsonString="["+resultjsonString+"]";
			resultxmlString="<?xml version=\"1.0\"?><outputXML>"+resultxmlString+"</outputXML>";
			System.out.println("result JSON:: "+resultjsonString);
			System.out.println("result XML:: "+resultxmlString);
			String resultjsonKeyString="{\"total\":10,\"rows\":"+resultjsonString+"}";
				
			PrintWriter out;
		
			out = response.getWriter();
			
			if(exportType.equals("xml"))
			{
				System.out.println("Sending XML");
				out.println(resultxmlString);
			}
			if(exportType.equals("text/html"))
			{
				System.out.println("Sending Datagrid");
				out.print(resultjsonKeyString);
			}
			else if(exportType.equals("Excel"))
			{
				System.out.println("Sending CSV");
				/*String resultjsonKeyString="{\"data\":"+resultjsonString+"}";
				JSONObject json = new JSONObject(resultjsonKeyString);
	            String resultCSVString=(CDL.toString(new JSONArray(json.get("data").toString())));
				out.println(resultCSVString);*/
				
				 String resultTxtString=txtHead+"\n"+txtBody;
					out.println(resultTxtString);
			}
			else if(exportType.equals("txt"))
			{
				System.out.println("Sending txt");
				
	            String resultTxtString=txtHead+"\n"+txtBody;
				out.println(resultTxtString);
			}
				
			
			
				return out;
				
		} 
		catch (ClassNotFoundException  e) 
		{
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}catch (JSONException e) {
				
			e.printStackTrace();
			return null;
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		
		
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Reached Dyna doPost method");
		
		String Source=request.getParameter("Src");
		String exportType=request.getParameter("exportType");
		
		System.out.println("Source = "+Source);
		
			
		if(exportType.equals("Excel")){
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment;filename=Report.xls");
		}
		else if(exportType.equals("xml")){
			response.setHeader("Content-Disposition","attachment; filename=\"Result.xml\"");
			response.setContentType("application/xml");
		}
		else if(exportType.equals("txt")){
			response.setHeader("Content-Disposition","attachment; filename=\"Response.txt\"");
			response.setContentType("application/octet-stream");
		}
		else if(exportType.equals("text/html")){
			
		}
		else
		{
			System.out.println("Unknown Source");
			return;
		}
		
			System.out.println("Export to "+exportType);
			PrintWriter out = response.getWriter();
			out=makeDynamicDG(request,response,exportType);
			
			
		
		
		
		
		
	}

}


