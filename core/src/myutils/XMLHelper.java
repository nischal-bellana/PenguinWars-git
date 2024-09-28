package myutils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer; 
import javax.xml.transform.TransformerFactory; 
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult; 
import org.w3c.dom.Document; 
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.penguin_wars.game.Constants;

public class XMLHelper {
	public static void xmlWrite(Array<PNadv> polys,String path) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document doc = builder.newDocument();
		
		Element polygons = doc.createElement("polygons");
		doc.appendChild(polygons);
		
		for(PNadv head:polys) {
			Element polygon = doc.createElement("polygon");
			PointNode cur = head;
			polygon.appendChild(doc.createTextNode(""+cur.x+" "+cur.y));
			cur = cur.next;
			while(cur!=head) {
				polygon.appendChild(doc.createTextNode(" "+cur.x+" "+cur.y));
				cur = cur.next;
			}
			polygons.appendChild(polygon);
		}
		TransformerFactory tfac = TransformerFactory.newInstance();
		Transformer trans = tfac.newTransformer();
		DOMSource src = new DOMSource(doc);
		
		StreamResult res = new StreamResult(path);
		trans.transform(src, res);
		
		System.out.println("\nWrite Success");
	}
	public static List<float[]> xmlRead(String path,float[] bound) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		FileHandle fileHandle = Gdx.files.absolute(Constants.workdir+path);
		File fxml = fileHandle.file();
		
		Document doc = builder.parse(fxml);
		List<float[]> polys = new ArrayList<>();
		NodeList polylist = doc.getElementsByTagName("polygon");
		for(int i=0;i<polylist.getLength();i++) {
			Node node = polylist.item(i);
			float[] poly = parseNode(node,bound);
			polys.add(poly);
		}
		return polys;
	}
	public static float[] parseNode(Node node,float[] bound) {
		String str = node.getTextContent();
		List<Float> poly = new ArrayList<Float>();
		int i=0;
		int j=1;
		while(i<str.length()) {
			while(j<str.length() && str.charAt(j)!=' ') {
				j++;
			}
			poly.add(Float.parseFloat(str.substring(i, j)));
			j++;
			i=j;
		}
		boolean isx = true;
		float[] a = new float[poly.size()];
		for(i=0;i<a.length;i++) {
			a[i] = poly.get(i);
			bounch(bound,a[i],isx);
			isx = !isx;
		}
		return a;
	}
	public static void bounch(float[]bound,float f, boolean isx) {
		if(isx) {
			if(f<bound[0]) {
				bound[0] = f;
			}
			if(f>bound[1]) {
				bound[1] = f;
			}
		}
		else {
			if(f<bound[2]) {
				bound[2] = f;
			}
			if(f>bound[3]) {
				bound[3] = f;
			}
		}
	}
	public static boolean compareArrs(List<float[]>polys,List<float[]>polys2) {
		if(polys.size()!= polys2.size()) {
			return false;
		}
		for(int i=0;i<polys.size();i++) {
			float[] poly = polys.get(i);
			float[] poly2 = polys2.get(i);
			if(poly.length!=poly2.length) {
				return false;
			}
			for(int j=0;j<poly.length;j++) {
				if(poly[j]!=poly2[j]) {
					return false;
				}
			}
			
		}
		return true;
	}
}