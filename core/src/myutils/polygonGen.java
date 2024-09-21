 package myutils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.penguin_wars.game.cookie;

public class polygonGen {

	public static void trcImg(String path,String output) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = Imgcodecs.imread(path);
        // Convert image to grayscale
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        // Threshold the grayscale image
        Mat thresholded = new Mat();
        Imgproc.threshold(grayImage, thresholded, 10, 255, Imgproc.THRESH_BINARY);

        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(thresholded, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Initialize list to store polygon vertices
        List<List<Point>> polygonVertices = new ArrayList<>();

        // Iterate through each contour
        for (MatOfPoint contour : contours) {
            // Approximate the contour to a polygon
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            double epsilon = 0.002 * Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true);
            Imgproc.approxPolyDP(new MatOfPoint2f(contour.toArray()), approxCurve, epsilon, true);

            // Filter out small polygons
            if (Imgproc.contourArea(approxCurve) > 100) {
                // Convert polygon points to a list of Point objects
                List<Point> polygon = new ArrayList<>();
                for (Point point : approxCurve.toList()) {
                    polygon.add(point);
                }
                polygonVertices.add(polygon);
            }
        }
        // Display the vertices of each polygon
        Array<PNadv> polys = trcHelp(polygonVertices,image.rows());
        System.out.println("polynodes created!");
        try {
        	XMLHelper.xmlWrite(polys, output);
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        return;
	}
	static Array<PNadv> trcHelp(List<List<Point>> polygonV,double height){
		Array<PNadv> polys = new Array<PNadv>();
		for(int i=0;i<polygonV.size();i++) {
			PNadv head = PNadv.listoNode(polygonV.get(i),height);
			System.out.println("decomp started!");
			Decomp.decomp(head,polys);
			System.out.println("decomp ended!");
		}
		return polys;
	}
	
	public static Body createImgb(World world,String path,Body body,FixtureDef fdef,float scale,float[] bound) {
		List<float[]> polys = new ArrayList<>();
		bound[0] = bound[2] = Float.MAX_VALUE;
		bound[1] = bound[3] = Float.MIN_VALUE;
		try {
	        polys = XMLHelper.xmlRead(path,bound);
	    }
	    catch(Exception e) {
	        	e.printStackTrace();
	    }
		bound[0] = (bound[0]+bound[1])/2;
		bound[1] = (bound[2]+bound[3])/2;
		
		for(float[] poly: polys) {
			Polygon polyscld = new Polygon(poly);
			polyscld.setPosition(-bound[0]*scale, -bound[1]*scale);
			polyscld.setScale(scale, scale);
			PolygonShape pshp = new PolygonShape();
			float[] trs = polyscld.getTransformedVertices();
			if(!cookie.validarr(trs)) {
				pshp.dispose();
				continue;
			}
			pshp.set(trs);
			fdef.shape = pshp;
			body.createFixture(fdef).setUserData(body.getUserData());
			pshp.dispose();
		}
		return body;
	}
}
