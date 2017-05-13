/**
 * 
 */
package mainSystem;


import org.jfree.ui.RefineryUtilities;

/**
 * @author ac
 *
 */
public class CreateView {
	public static void createChart(String[]timestamp,double[]pm2_5,int[]aqi){
		CombinedCategoryPlotDemo1 demo = new CombinedCategoryPlotDemo1(timestamp,pm2_5,aqi);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
	}

}
