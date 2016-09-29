

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Random;

import javax.imageio.ImageIO;
//import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import android.util.Base64;
//import org.apache.commons.codec.binary.*;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;

//import javax.servlet.RequestDispatcher;

/**
 * Servlet implementation class ReadAjaxParameters
 */
@WebServlet("/dummy")
public class DummyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DummyServlet() {
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
		System.out.println("Reached DUMMY doGet method");
		/*pageNum=Integer.parseInt(request.getParameter("pageNum"));
		pageSize=Integer.parseInt(request.getParameter("pageSize"));
		System.out.println("Page Number Dummy:"+pageNum);
		System.out.println("Page Size:"+pageSize);
		
		
		response.sendRedirect("webpages/ProgressBarTest.html?Count=763&pageSiz="+pageSize+"&pageNum="+pageNum);*/
		doPost(request,response);
       
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void getData(HttpServletRequest request,HttpServletResponse response)
	{
		String data=request.getParameter("data");
		System.out.println("File Data is:" +data);
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	public void getImage(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String data=request.getParameter("data");
		System.out.println("File Data is_:" +data);
		
		String imgdata = data.substring(data.indexOf(",")+1);//get data after "," and get a new stream
		
		
		System.out.println();
		System.out.println("Servlet DataString_:"+imgdata);
		
		imgdata= imgdata.substring(0, imgdata.length() - 2);//imgdata has 2 "=" at the end
		System.out.println("String length="+imgdata.length());
		imgdata=imgdata.replace("/", "_");// Expected string has _ and -
		imgdata=imgdata.replace(" ", "-");
		
		
		try
		{
			byte[] imageByteArray = Base64.decodeBase64(imgdata);
	    
        // Write a image byte array into file system
        FileOutputStream imageOutFile = new FileOutputStream(
                "C:\\Users\\\\Desktop\\out6.png");

        imageOutFile.write(imageByteArray);

        
        imageOutFile.close();

        System.out.println("Image Written");
    } catch (FileNotFoundException e) {
        System.out.println("Image not found" + e);
    } catch (IOException ioe) {
        System.out.println("Exception while reading the Image " + ioe);
    }
		
		 
		/* byte[] imageByte = Base64.decodeBase64(data);    
		 //String directory = "D:\\Image Capture\\sample.png ";
		 String directory = " C:\\Users\\SK28838\\Desktop\\ServletOut.png";
		 FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(directory);
			 outputStream.write(imageByte);
			 outputStream.flush();
			 outputStream.close();
			 System.out.println("Image saved");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Image save failed");
		} */           
		
		 //String imageDataBytes = data.substring(data.indexOf(",")+1);//get data after "," and get a new stream
		 //InputStream imgstream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
		 
		 
		/* byte[] bitImg= Base64.decodeBase64(data);
		 String directory = "C:\\Users\\\\Desktop\\ServletOut.png";
		 OutputStream stream;
			try {
				stream = new FileOutputStream(directory);
				stream.write(bitImg);
				stream.flush();
				stream.close();
				System.out.println("file saved");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("Image save failed");
			}*/
		 
		 
		 
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public void getDate(HttpServletRequest request,HttpServletResponse response)
	{
		String dateF=request.getParameter("dateF");
		String monthF=request.getParameter("monthF");
		String yearF=request.getParameter("yearF");
		String dateT=request.getParameter("dateT");
		String monthT=request.getParameter("monthT");
		String yearT=request.getParameter("yearT");
		System.out.println("in getDate(): From:"+dateF+"/"+monthF+"/"+yearF+">>>To:"+dateT+"/"+monthT+"/"+yearT);
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print("got the Dates");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Reached DUMMY doPost method");
		
		String Source=request.getParameter("Src");
		System.out.println("Source ="+Source);
		if(Source.equals("File"))
		{
			getData(request,response);
			
		}
		if(Source.equals("DateBox"))
		{
			getDate(request,response);
			
		}
		if(Source.equals("Image"))
		{
			getImage(request,response);
			
		}
		
		
		
		
		
	}

}

