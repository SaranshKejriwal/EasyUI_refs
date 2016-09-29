
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Servlet implementation class Treegrid
 */
@WebServlet("/tree")
public class Infi_TreeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Infi_TreeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Reached TREE doGet method");
		// TODO Auto-generated method stub
	}
	public int pageNum=1,pageSize=15;
	public int for_local=0;//incremented in getLocal()
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public String randomname() 
	{
		StringBuffer s=new StringBuffer();
		for(int i=0;i<5;i++)
		{
			Random r=new Random();
			char n=(char)(r.nextInt(25) + 97);//keeps value between 97 and 122 (a-z as per ASCII)
			s.append(n);
		}
		
		return s.toString();
	}
	public String randomid() 
	{		
			
			Random r=new Random();
			char n1=(char)(r.nextInt(8) + 49);
			char n2=(char)(r.nextInt(9) + 48);
			String o=n1+""+n2+"";
			return o;
	}	
	public String randomsize() 
	{
		
			
			Random r=new Random();
			char n1=(char)(r.nextInt(8) + 49);
			char n2=(char)(r.nextInt(9) + 48);
			
			String o=n1+""+n2+" KB";
		
		return o;
	}	
	public String randomdate() 
	{
		
			
			Random r=new Random();
			char n1=(char)(r.nextInt(8) + 49);//[1-9]
			char n2=(char)(r.nextInt(9) + 48);//[0-9]
			char n3=(char)(r.nextInt(1) + 49);//[1-2]
			String o=n3+""+n2+"/0"+n1+"/20"+n3+n2;
		
		return o;
	}	
	public String randomstatus() 
	{
		Random r=new Random();
		int a=(r.nextInt(3) + 1);// a is in range [1,3]
		//System.out.println(a);
		
		if(a==2)
		{	return "open";}
		if(a==3)
		{	return "closed";}
		
		else
		{	return "";}
	}
		/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	public void getTreePagination(HttpServletRequest request,HttpServletResponse response)
	{
		if(request.getParameter("pageNum")==null)
		{
			pageNum=1;
			System.out.println("pagenum is null (Read DoPost)");
		}
		else
		{
			pageNum=Integer.parseInt(request.getParameter("pageNum"));
		}	
		/*Note, the database will be sending an Arraylist of its own => bean_num=list.size(), not a constant*/
		//ProductBO[] bean = new ProductBO[bean_num];
		System.out.println("Page Number DoPost:"+pageNum);
		System.out.println("Page Size DoPost:"+pageSize);
		ArrayList<TreeBO> Parents = new ArrayList<TreeBO>(); //big list, has all array elements
		System.out.println("into TreeOpsDAO");
		TreeOpsDAO DB1=new TreeOpsDAO();
		
		Parents=DB1.getDBTreeParent(pageNum,pageSize);
	
		String outS="[";
		
		for(int i=0;i<Parents.size();i++)
		{
			outS=outS+MakeTree.makeString(Parents.get(i));
			if(i!=Parents.size()-1)
			{
				outS=outS+",";
			}
			
		}
		outS=outS+"]";
		
		
		10
		/*json.put("total", DB1.getRecCount());// to load Full bean list
		json.put("rows", beanListFull);*/
		JSONObject json = new JSONObject();
		
		
	
		try {
			System.out.println("Rec Count ="+DB1.getRecCount());
			json.put("total", DB1.getRecCount());
			json.put("par", Parents.size());
			json.put("rows", Parents);
			PrintWriter out = response.getWriter();
			
			out.print(json);
			System.out.println("JSON populated");
		} catch (JSONException|IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Json failed");
		}
		
		
		
		
	}
	
	
	
	public void getPageTotal(HttpServletRequest request,HttpServletResponse response)
	{
		
		
		System.out.println("into TreeOpsDAO");
		TreeOpsDAO DB1=new TreeOpsDAO();
		int count=DB1.getRecCount();
		
		
		System.out.println("PageTotal = "+count);
		
			
	
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(count);
				System.out.println("Total count returned");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
				
	}

	public void getChild(HttpServletRequest request,HttpServletResponse response)
	{
		String parent=request.getParameter("parent");
		int level=Integer.parseInt(request.getParameter("level"));
		
			
		System.out.println("in TreeServlet getChild() of "+parent);
		System.out.println("level: "+level);
		
				
		
		ArrayList<TreeBO> Children = new ArrayList<TreeBO>(); //big list, has all array elements
		System.out.println("into TreeOpsDAO");
		TreeOpsDAO DB1=new TreeOpsDAO();
		
		Children=DB1.getDBChild(parent,level);
	
		String outS="[";
		
		for(int i=0;i<Children.size();i++)
		{
			outS=outS+MakeTree.makeString(Children.get(i));
			if(i!=Children.size()-1)
			{
				outS=outS+",";
			}
			
		}
		outS=outS+"]";
		
		System.out.println("outS is"+outS);
		
		/*json.put("total", DB1.getRecCount());// to load Full bean list
		json.put("rows", beanListFull);*/
		JSONObject json = new JSONObject();
		
		
	
		try {
			//System.out.println("Rec Count ="+DB1.getRecCount());
			json.put("total", DB1.getRecCount());
			json.put("rows", Children);
			PrintWriter out = response.getWriter();
			
			out.print(outS);
			System.out.println("JSON populated");
		} catch (JSONException|IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Json failed");
		}
		
		
		
		
	}
	public void getSearch(HttpServletRequest request,HttpServletResponse response)
	{
		String Query=request.getParameter("Query");
		String TimeStamp=request.getParameter("REC_CREATION_TS");
		if(Query.length()!=0 || TimeStamp.length()!=0)
		{
		
		String QueryU = Query.substring(0,2).toUpperCase() + Query.substring(2);//convert first 2 letters to uppercase
		
		System.out.println("Searching User ID "+QueryU);
		System.out.println("Timestamp: "+TimeStamp);
		
				
		
		ArrayList<TreeBO> Results = new ArrayList<TreeBO>(); //big list, has all array elements
		System.out.println("into TreeOpsDAO");
		TreeOpsDAO DB1=new TreeOpsDAO();
		
		Results=DB1.getSearchResult(QueryU,TimeStamp);
	
		String outS="[";
		
		for(int i=0;i<Results.size();i++)
		{
			outS=outS+MakeTree.makeString(Results.get(i));
			if(i!=Results.size()-1)
			{
				outS=outS+",";
			}
			
		}
		outS=outS+"]";
		
		System.out.println(outS);
		
		/*json.put("total", DB1.getRecCount());// to load Full bean list
		json.put("rows", beanListFull);*/
		JSONObject json = new JSONObject();
		
		
	
		try {
			System.out.println("Rec Count ="+Results.size());
			json.put("total", Results.size());
			json.put("rows", Results);
			PrintWriter out = response.getWriter();
			
			out.print(json);
			System.out.println("JSON populated");
		} catch (JSONException|IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Json failed");
		}
		
	}
		else
		{
			System.out.println("Search Query Null: Going to Pagination");
		}
		
		
	}
	
	
	public void getLocal(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		for_local++;
		int lev=Integer.parseInt(request.getParameter("level"));
		//System.out.println("in getLocal: Level is "+lev);
			
		TreeBO[] levBean= new TreeBO[8];//array index will decide the number of children in ANY parent
		
		for(int i=0;i<levBean.length;i++)
		{
			levBean[i]=MakeTree.makeNode(lev+""+i+""+for_local, "Bean Level "+lev+"_"+(i+1),"","","","","","","","","");
			//all ID's should be unique,hence global variable for_local is incremented with every getLocal() call
			
		}
		
		
		
		String outS=null;
		outS="[";
		
		String outT="[";
		for(int i=0;i<levBean.length;i++)
		{
					outT=outT+""+MakeTree.makeString(levBean[i])+"";
					if(i!=levBean.length-1)
					{
						outT=outT+",";
					}
		}
		outT=outT+"]";
		outS="["+MakeTree.makeString(levBean[0])+"]";
		//System.out.println("outS="+outS);
		System.out.println("outT="+outT);
		
		//outS=outT;
		try {
			
			//outS="["+MakeTree.makeString(levBean[0])+","+MakeTree.makeString(levBean[1])+"]";
			
			//System.out.println("sent to JSON: "+outT);
			PrintWriter out = response.getWriter();
			out.print(outT);
			System.out.println("out printed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("can't make child");
		}
		
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Reached TREE doPost method");
				
		String Source=request.getParameter("Src");
		System.out.println("Source = "+Source);
		
		if(Source.equals("Pagination"))
		{
			getTreePagination(request,response);
		}
		if(Source.equals("PageTotal"))
		{
			getPageTotal(request,response);
		}
		if(Source.equals("GetChild"))
		{
			getChild(request,response);
		}
		if(Source.equals("Search"))
		{
			getSearch(request,response);
		}
		if(Source.equals("Local"))
		{
			int level=Integer.parseInt(request.getParameter("level"));
			System.out.println("in doPost: level="+level);
			getLocal(request,response);
		}
		
		/*TreeBO t1=new TreeBO();
		TreeBO t2=new TreeBO();
		TreeBO t3=new TreeBO();
		TreeBO t4=new TreeBO();
		TreeBO t5=new TreeBO();
		
		t1=MakeTree.makeNode("1", "t1", true, "25-June-15");
		t2=MakeTree.makeNode("2", "t2", false, "14");
		t3=MakeTree.makeNode("3", "t3", true, "15");
		t4=MakeTree.makeNode("4", "t4", false, "16");
		t5=MakeTree.makeNode("5", "t5", false, "17");
		
		t2.makeChildOf(t1);
		
		t4.makeChildOf(t3);
		t3.makeChildOf(t1);*/
		
		
		
		/**
		TreeBO[] bean = new TreeBO[bean_num];
		ArrayList<TreeBO> beanListFull = new ArrayList<TreeBO>(); //big list, has all array elements
				
		for(i=0;i<bean_num;i++)
		{
			bean[i]=new TreeBO();
			Random rn = new Random();
			String idr = randomid();
			String namer=randomname();
			String sizer=randomsize();
			String statusr=randomstatus();
			String dater=randomdate();
			bean[i]=MakeTree.makeTree(idr, namer, sizer, dater,statusr);
			beanListFull.add(bean[i]);
		}*/
		
		/*List<ProductBO>[] minBeanList = new ArrayList<ProductBO>[list_num];		
		Java cannot create an array of Arraylist. It's better to create an Arraylist of Arraylist*/
		
		/*if bean_num varies, we can fix subListSize and recalculate new list_num
		 * Caution: use Math.ceil() NOT Math.floor, else we lose beans*/
		/*String outS=null;
		//outS="["+MakeTree.makeString(t1)+","+MakeTree.makeString(t5)+"]";
		System.out.println("sent to JSON: "+outS);
		PrintWriter out = response.getWriter();
		out.print(outS);
		System.out.println("out printed");*/
			
		
		
	}

}

