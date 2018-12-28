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
 * 10�� 18�� xml���� ��� attribute�� ��������, db(mysql)�����ؼ� �� �־��ֱ�ϼ�
 */
//xml������ �о�´�.
public class OpenXml {

		public static void main(String argv[]){ //������ڰ� 1��
			
			try{
				int ObjectRectLeft=0;
				int ObjectRectRight=0;
				int ObjectRectTop=0;
				int ObjectRectBottom=0;
				int count=1;
				int Pagenumberid=0;
				//�� ������ xml���� ��ŭ ������.
				for(Pagenumberid=316; Pagenumberid<501; Pagenumberid++ ){
					//315����
				
//				String XmlUrl = "/result"+Pagenumberid+".xml";

				String XmlUrl = "C:/Users/CKH/vips_css/vips_java/result"+Pagenumberid+".xml";
				
				
				File fXmlFile = new File(XmlUrl); //��ο��� xml������ �����´�/
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();//xml�������� DOM��ü Ʈ���� �����ϴ� �ļ��� ���� �� �ְ��ϴ� ���丮 API����
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); //���丮�ȿ� �մ� DocumentBuilder�� ���� 
				//DocumentBuilder�� ����ϸ� xml���� ������ �����ü��ִ�.
				if(fXmlFile.isFile()){
				Document doc = dBuilder.parse(fXmlFile); //xml���� �����ͼ� DOM��ü��
				
				System.out.println("Root element : " + doc.getDocumentElement().getNodeName()); //��Ʈ ��� �̸� ���� �˷��� �� xml������ VIPSPage �̰ڱ�.
				NodeList nList = doc.getElementsByTagName("LayoutNode");   //LayoutNode�̸��� ������ NodeList�� �ִ´�.
				System.out.println("------------");
				
				for(int temp=0; temp<nList.getLength(); temp++){
					Node nNode = nList.item(temp); //��� ����Ʈ�� �մ� ��帮��Ʈ[temp]�� �� ���(nNode)���� ��´�.
					if(nNode.getNodeType() == Node.ELEMENT_NODE){ //nNode�� Ÿ���� ELEMENT_NODE�� Ÿ���̶��
//						ELEMENT_NODE�� ����,����?
						Element eElement = (Element) nNode;
						//xml�� �Ӽ����� �޾ƿ´�.
						ObjectRectLeft = Integer.parseInt(eElement.getAttribute("ObjectRectLeft"));
//						System.out.println("ObjectRectLeft: "+ ObjectRectLeft);
						ObjectRectRight =  Integer.parseInt(eElement.getAttribute("ObjectRectLeft")) +  Integer.parseInt(eElement.getAttribute("ObjectRectWidth"));
//						System.out.println("ObjectRectRight: "+ ObjectRectRight);
						ObjectRectTop = Integer.parseInt(eElement.getAttribute("ObjectRectTop")); 
//						System.out.println("ObjectRectTop: "+ ObjectRectTop);
						ObjectRectBottom = Integer.parseInt(eElement.getAttribute("ObjectRectTop")) +  Integer.parseInt(eElement.getAttribute("ObjectRectHeight"));
//						System.out.println("ObjectRectBottom: "+ ObjectRectBottom);
						
						//���� DB���������� , mysql ��ġ��  mysql-connector-java-8.0.12 ���̺귯�� ����	

						//MySQL ���� 				
						Connection connection = null;
						Statement st = null;
						
						try{ 
							Class.forName("com.mysql.cj.jdbc.Driver");
							connection = DriverManager.getConnection("jdbc:mysql://166.104.144.72:3306/experiment_framework_test?serverTimezone=UTC&useSSL=false", "dblab","9184");
//							System.out.println("����");
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
				
				System.out.println("����:"+(count));
				System.out.println(Pagenumberid+"��°������");
			}
			}	
			}catch(Exception e){
					e.printStackTrace();
			
			}
			
		}
		
}
