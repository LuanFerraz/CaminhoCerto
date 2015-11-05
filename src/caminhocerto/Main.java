package caminhocerto;

import forms.JFLogin;
import java.sql.SQLException;
import javax.swing.JOptionPane;
// @author Renan

public class Main {
   // @param args the command line arguments
    
public static void main(String[] args) {
        JFLogin frmlogin;
        try {
            frmlogin = new JFLogin();
            frmlogin.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Problema ao carregar formulário de login.\n"+ex,"Erro",JOptionPane.WARNING_MESSAGE);
        }
        
        
    }

}
