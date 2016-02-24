
import java.awt.Point;
import java.awt.Polygon;


public class Landscape {
    private Polygon p;


    public Landscape(){
        p = new Polygon();
    }

    public Landscape(Polygon p){
        this.p = p;
    }

    public void addPointToLandscape(int x, int y){
        p.addPoint(x, y);
    }


    /*protected void paint(Graphics g){

        super.paint(g);

        g.setColor(Color.BLUE);
        g.drawPolygon(poly);

    }*/

}
