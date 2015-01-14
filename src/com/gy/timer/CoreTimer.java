package com.gy.timer;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
 
public class CoreTimer extends TimerTask {
	private ServletContext servletContext;
	private static boolean isRunning = false;
	public static int sendcount = 0;
	
	HashMap<String ,GyTimerJob> gytimerjobs = new HashMap<String ,GyTimerJob>(); 
	public CoreTimer(ServletContext servletContext) {		  
	    String savePath = servletContext.getRealPath("/");   
 		this.servletContext = servletContext; 
		File file =new File(savePath+"/WEB-INF/gyTimerConfig.xml");
//		File file =new File("/WEB-INF/gyTimerConfig.xml");
		 SAXReader saxReader = new SAXReader();
	        Document doc=null;
			try {
				doc = saxReader.read(file);
			} catch (DocumentException e1) { 
				e1.printStackTrace();
			}
	        //��ȡ��Ԫ��
	        Element root = doc.getRootElement();
	        //��ȡ��Ԫ���µ����нڵ�  ��Timers�ڵ� 
	        for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
	            Element e = (Element) iterator.next(); 
	            //����student�ڵ��µ����нڵ�
                String jobName=""  ;
                int jobId=1,interVal=10000;
	            for (Iterator iterator2 = e.elementIterator(); iterator2.hasNext();) {
	                Element ee = (Element) iterator2.next();
	                //��ȡ�ڵ����� ��ֵ  
	                if (ee.getName().equals("jobId")) {
//	                	job.setJobId(Integer.valueOf(ee.getText() ));
	                	jobId=Integer.valueOf(ee.getText() );
					}else if (ee.getName().equals("jobName") ) {
//						job.setJobName( ee.getText());
						System.err.println( "jobName1"+ee.getText() );
						jobName=  ee.getText();
					}else if (ee.getName().equals("interVal") ) {
//						job.setInterVal(Integer.valueOf(ee.getText() ));
						interVal = Integer.valueOf(ee.getText() );
					}
	            } 
                GyTimerJob job = new GyTimerJob(jobName,interVal,jobId);	       
                gytimerjobs.put( job.getJobName() , job);
	        }
	}
	/**
	 * @param argss
	 */
	public static void main(String[] args) { 
		CoreTimer c = new CoreTimer(null);
		System.err.println(c.gytimerjobs); 
		c.run();
	}

	 
	@Override
	public void run() {
		System.err.println("CoreTimerִ������-------------------------------------");
		Iterator<?> itr =  gytimerjobs.entrySet().iterator();
        while (itr.hasNext()) { 
            Map.Entry entry = (Map.Entry) itr.next(); 
            GyTimerJob job = (GyTimerJob)entry.getValue();
            job.run();
//            if (list1.get(entry.getKey())!=null ) { 
//            	listintersection.put((String)entry.getKey()  ,  list1.get(entry.getKey()));            	
//    			}
			}    
	}

}
