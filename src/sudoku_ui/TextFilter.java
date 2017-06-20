package sudoku_ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class TextFilter  extends DocumentFilter {
	

       @Override  
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException  
        {
            //System.out.println("Inserting text");
    	   	if(fb.getDocument().getLength()+string.length()>1)
            {
                return;
            }
            else if ("1".compareTo(string) < -8 || "1".compareTo(string)>0)
           	 
            {
            	
            	string = "";
            }

            fb.insertString(offset, string, attr);

        }  


        @Override  
        public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException 
        {  
        	//System.out.println("Removing text");
            fb.remove(offset,length);
        }  


        @Override  
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)throws BadLocationException 
        {  

           // System.out.println("replacing text");
            //System.out.println("\"" +text +"\"");

                 if(fb.getDocument().getLength()+text.length()>1)
                 {
                	 //System.out.println("longtext: " + " offset " +offset + " length " + length);
                    return;
                 }
                 else if ("1".compareTo(text) < -8 || "1".compareTo(text)>0)
                 {
                	 if (fb.getDocument().getLength()==1)
                		 fb.remove(offset, length);
                	 else
                		 text = "";
                 }

                fb.insertString(offset, text, attrs);
        }
    }

  



