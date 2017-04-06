/** QuickPALM plugin developed by Ricardo Henriques @ Instituto de Medicina 
 *  Molecular (PT)/Institut Pasteur (FR).
 *  Adapted under GPL by Marcel Stefko, 2017.
 */ 

package algorithm_tester.analyzers.quickpalm;



import ij.*;
import ij.process.*;

/** This plugin detectes sub-diffraction particles in a sequence of images, it
 * is the main plugin for the QuickPALM package.
*/
public class Analyse_Particles
{
	ImagePlus imp;
	ImageProcessor ip;
	
	MyDialogs dg = new MyDialogs();
	MyFunctions f = new MyFunctions();
	MyIO io = new MyIO();

    /**
     * Analyzes particles
     * @param arg
     */
    public void run(String arg) 
	{
		IJ.register(Analyse_Particles.class);
		if (!dg.analyseParticles(f)) return;
		
		f.ptable.reset(); // erase particle table
		
		if (dg.is3d)
		{
			dg.getCalibrationFile();
			io.loadTransformation(dg.calfile, f.caltable);
			f.initialize3d();
		}
		
		if (dg.attach)
		{
			dg.getImageDirectory();
			imp=f.getNextImage(dg, 0);
			if (imp==null)
			{
				IJ.error("could not find image following given pattern");
			    return;
			}
		}
		else
		{
			imp = IJ.getImage();
			if (imp==null)
			{
			    IJ.noImage();
			    return;
			}
			else if (imp.getType() != ImagePlus.GRAY8 && imp.getType() != ImagePlus.GRAY16 ) 
			{
			    IJ.error("8 or 16 bit greyscale image required");
			    return;
			}
		}
	
		
		ProcessFrame [] threads = new ProcessFrame[dg.threads];
		int freeThread=-1;
		
		long time_start = java.lang.System.currentTimeMillis();
		long time_took = 0;
		long time_now=0;
		long nparticles=0;

		int s=0;
		boolean ok = true;
		
		while (ok)
		{
			if (dg.attach)
			{
				imp=f.getNextImage(dg, s);
				if (imp==null) ok=false;
				else ip=imp.getProcessor();
			}
			else
			{
				if (s>=imp.getStackSize()) ok=false;
				else
				{
					imp.setSlice(s+1);
					ip=imp.getProcessor().duplicate();
				}
			}
			
			if (ok)
			{
				if (s<threads.length)
					freeThread=s;
				else
				{
					freeThread=-1;
					while (freeThread==-1)
					{
						for (int t=0;t<threads.length;t++)
						{
							if (!threads[t].isAlive())
							{
								freeThread=t;
								break;
							}
						}
						if (freeThread==-1)
						{
							try
							{
								Thread.currentThread().sleep(1);
							}
							catch(Exception e)
							{
									IJ.error(""+e);
							}
						}
					}
				}
			
				threads[freeThread] = new ProcessFrame();
				threads[freeThread].mysetup(ip, f, dg, s);
				threads[freeThread].start();
				
				time_now = java.lang.System.currentTimeMillis();
				time_took += time_now-time_start;
				time_start = time_now;
				if ((s>0) && (s%dg.viewer_update==0))
				{
					ij.IJ.showStatus("Processing at "+time_took/dg.viewer_update+" ms/frame "+(f.ptable.getCounter()-nparticles)/dg.viewer_update+" part/frame, detected "+nparticles+" particles");
					nparticles=f.ptable.getCounter();
					time_took=0;
				}
			}
			s++;
		}
		for (int t=0; t<threads.length;t++)
		{
			try
			{
				threads[t].join();
			}
			catch(Exception e)
			{
				IJ.error(""+e);
			}
		}
		if (f.ptable.getCounter()<5000000)
		{
            IJ.showStatus("Creating particle table, this should take a few seconds...");
			f.ptable.show("Results");
		}
        else
            IJ.showMessage("Warning", "Results table has too many particles, they will not be shown but the data still exists within it\nyou can still use all the plugin functionality or save table changes though the 'Save Particle Table' command.");

	}
}

class ProcessFrame extends Thread 
{
	private ImageProcessor ip;
	private MyDialogs dg;
	private int frame;
	private MyFunctions f;
	
	public void mysetup(ImageProcessor ip, MyFunctions f, MyDialogs dg, int frame)
	{
		this.f=f;
		this.ip=ip;
		this.dg=dg;
		this.frame=frame;
	}
	
	public void run()
	{
		this.f.detectParticles(this.ip, this.dg, this.frame);
	}
}
