import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/*
 * 10월 18일 xml파일 열어서 attribute값 가져오고, db(mysql)연동해서 값 넣어주기완성
 */
//xml파일을 읽어온다.
public class OpenXml {

		public static void main(String argv[]){ //명령인자가 1개
			
			try{
				int ObjectRectLeft=0;
				int ObjectRectRight=0;
				int ObjectRectTop=0;
				int ObjectRectBottom=0;
				int count=1;
				int Pagenumberid=0;
				//각 페이지 xml개수 만큼 돌린다.
				for(Pagenumberid=316; Pagenumberid<501; Pagenumberid++ ){
					//315오류
				
//				String XmlUrl = "/result"+Pagenumberid+".xml";

				String XmlUrl = "C:/Users/CKH/vips_css/vips_java/result"+Pagenumberid+".xml";
				
				
				File fXmlFile = new File(XmlUrl); //경로에서 xml파일을 가져온다/
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();//xml문서에서 DOM객체 트리를 생성하는 파서를 얻을 수 있게하는 팩토리 API정의
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); //팩토리안에 잇는 DocumentBuilder를 생성 
				//DocumentBuilder를 사용하면 xml에서 문서를 가져올수있다.
				if(fXmlFile.isFile()){
				Document doc = dBuilder.parse(fXmlFile); //xml파일 가져와서 DOM객체로
				
				System.out.println("Root element : " + doc.getDocumentElement().getNodeName()); //루트 노드 이름 뭔지 알려줘 내 xml에서는 VIPSPage 이겠군.
				NodeList nList = doc.getElementsByTagName("LayoutNode");   //LayoutNode이름의 노드들을 NodeList에 넣는다.
				System.out.println("------------");
				
				for(int temp=0; temp<nList.getLength(); temp++){
					Node nNode = nList.item(temp); //노드 리스트의 잇는 노드리스트[temp]의 각 노드(nNode)들을 얻는다.
					if(nNode.getNodeType() == Node.ELEMENT_NODE){ //nNode의 타입이 ELEMENT_NODE의 타입이라면
//						ELEMENT_NODE의 범위,정의?
						Element eElement = (Element) nNode;
						//xml의 속성값을 받아온다.
						ObjectRectLeft = Integer.parseInt(eElement.getAttribute("ObjectRectLeft"));
//						System.out.println("ObjectRectLeft: "+ ObjectRectLeft);
						ObjectRectRight =  Integer.parseInt(eElement.getAttribute("ObjectRectLeft")) +  Integer.parseInt(eElement.getAttribute("ObjectRectWidth"));
//						System.out.println("ObjectRectRight: "+ ObjectRectRight);
						ObjectRectTop = Integer.parseInt(eElement.getAttribute("ObjectRectTop")); 
//						System.out.println("ObjectRectTop: "+ ObjectRectTop);
						ObjectRectBottom = Integer.parseInt(eElement.getAttribute("ObjectRectTop")) +  Integer.parseInt(eElement.getAttribute("ObjectRectHeight"));
//						System.out.println("ObjectRectBottom: "+ ObjectRectBottom);
						
						//이제 DB연동할차례 , mysql 설치후  mysql-connector-java-8.0.12 라이브러리 적용	

						//MySQL 연동 				
						Connection connection = null;
						Statement st = null;
						
						try{ 
							Class.forName("com.mysql.cj.jdbc.Driver");
							connection = DriverManager.getConnection("jdbc:mysql://166.104.144.72:3306/experiment_framework_test?serverTimezone=UTC&useSSL=false", "dblab","9184");
//							System.out.println("성공");
							st= connection.createStatement();
							
							String sql;
							sql = " INSERT INTO vips (PREDICT_METHOD, PREDICT_DOM, PREDICT_AREA_LEFT, "
									+ "PREDICT_AREA_RIGHT, PREDICT_AREA_TOP, PREDICT_AREA_BOTTOM, PAGE_NUMBER_ID) VALUES ('VIPS', 'TEXT',"+ ObjectRectLeft +"," + ObjectRectRight + " ,"+ObjectRectTop+","+ObjectRectBottom+","+Pagenumberid+")";

							st.executeUpdate(sql);
							
							ResultSet rs= st.executeQuery("SElECT * FROM vips");
							
							while(rs.next()){
								String sqlRecipeProcess1 = rs.getString("PREDICT_AREA_LEFT");
								String sqlRecipeProcess2 = rs.getString("PREDICT_AREA_RIGHT");
								String sqlRecipeProcess3 = rs.getString("PREDICT_AREA_TOP");
								String sqlRecipeProcess4 = rs.getString("PREDICT_AREA_BOTTOM");
								System.out.println(sqlRecipeProcess1+"  "+ sqlRecipeProcess2+"  " + sqlRecipeProcess3 +"  "+ sqlRecipeProcess4);
							}
							rs.close();
							st.close();
							connection.close();
							
						
							}catch(SQLException se1){
								se1.printStackTrace();
							}catch(Exception ex){
								ex.printStackTrace();
							}finally{
								try{
									if(st!=null){
										st.close();
									}
								}catch(SQLException se2){
									
								}
								try{
									if(connection!= null)
										connection.close();
								}catch(SQLException se){
									se.printStackTrace();
								}
								
							}
			
					}
					count+=1;
				}
				
				System.out.println("개수:"+(count));
				System.out.println(Pagenumberid+"번째페이지");
			}
			}	
			}catch(Exception e){
					e.printStackTrace();
			
			}
			
		}
		
}
