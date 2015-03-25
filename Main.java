import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main extends TimerTask{
  static Image image = Toolkit.getDefaultToolkit().getImage("icon.png");

  static TrayIcon trayIcon = new TrayIcon(image, "Tester2");

  public static void main(String[] a) throws Exception {
    if (SystemTray.isSupported()) {
      SystemTray tray = SystemTray.getSystemTray();

      trayIcon.setImageAutoSize(true);
	  
      trayIcon.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          System.out.println("In here");
          trayIcon.displayMessage("World Cup", "Some action performed", TrayIcon.MessageType.INFO);
        }
      });
      

      try {
        tray.add(trayIcon);
        trayIcon.displayMessage("World Cup", "I will alert you every 20 seconds :)", TrayIcon.MessageType.INFO);
        Timer timer = new Timer();
        timer.schedule(new Main(), 0, 20000);
        	
      } catch (AWTException e) {
        System.err.println("TrayIcon could not be added.");
      }
    }
  }

	@Override
	public void run() {
		
		String url = "http://static.cricinfo.com/rss/livescores.xml";
		
        try
        {
            DocumentBuilderFactory f = 
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(url);
 
            doc.getDocumentElement().normalize();
            System.out.println ("Root element: " + 
                        doc.getDocumentElement().getNodeName());
       
            // loop through each item
            NodeList items = doc.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++)
            {
                Node n = items.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                Element e = (Element) n;
 
                // get the "title elem" in this item (only one)
                NodeList titleList = 
                                e.getElementsByTagName("description");
                Element titleElem = (Element) titleList.item(0);
 
                // get the "text node" in the title (only one)
                Node titleNode = titleElem.getChildNodes().item(0);
                System.out.println(titleNode.getNodeValue());
                trayIcon.displayMessage("World Cup! :-)", titleNode.getNodeValue(), TrayIcon.MessageType.INFO);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
}