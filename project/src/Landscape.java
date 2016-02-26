
import java.awt.*;


public class Landscape {
    private Polygon p;
    private Color c;

    public Landscape(){
        p = new Polygon();
    }

    public Landscape(Polygon p){
        this.p = p;
        this.c = Color.LIGHT_GRAY;
    }

    public void addPointToLandscape(int x, int y)
    {
        p.addPoint(x, y);
    }

    public void setColorToDraw(Color c){
        this.c = c;
    }

    public Color getColor(){
        return c;
    }

    public Polygon getPolygon(){
        return p;
    }

    /*protected void paint(Graphics g){

        super.paint(g);

        g.setColor(Color.BLUE);
        g.drawPolygon(poly);

    }*/

}
