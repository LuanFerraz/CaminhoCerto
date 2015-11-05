package forms;

import atributos.atributos;
import bancodedados.ConexaoImpressao;
import bancodedados.criarConexao;
import bancodedados.motoristaDao;
import caminhocerto.Data;
import caminhocerto.Impressao;
import java.awt.Toolkit;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Gabriela
 */
public class JFPrincipal extends javax.swing.JFrame {
    
    DefaultTableModel tmMotoristas = new DefaultTableModel(null, new String []{"Código","Motorista","CNH","Vencimento"});
    List<atributos> motoristas;
    ListSelectionModel lsmMotoristas;

   private Connection conexao;
   Process proc;
   String opvenc="";
   
    public JFPrincipal() throws SQLException {
        initComponents();
        

         this.conexao = criarConexao.getConexao(); 
         
         mostraData();
         verificaVencimento();
         listarMotoristas();
         
        JFC_Backup.setFileFilter(new FileNameExtensionFilter(".sql", "sql"));
        JFC_Backup.setAcceptAllFileFilterUsed(false);
        JFC_Backup.setVisible(false);
         
         jTCarteiras.getColumnModel().getColumn(0).setPreferredWidth(1);
         jTCarteiras.getColumnModel().getColumn(1).setPreferredWidth(188);
         jTCarteiras.getColumnModel().getColumn(2).setPreferredWidth(40);
         //jTCarteiras.getColumnModel().getColumn(3).setPreferredWidth(30);
        
         this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagens/iconcc.png")));
         
        }
    
    
    public void mostraData(){
         Data mostra_data =new Data(); //cria objeto para inicializaçao da data
        mostra_data.data();
        
        jLDt.setText(" "+mostra_data.xDia+", "+mostra_data.dia+" de "+mostra_data.xMes+" de "+mostra_data.ano);//chama os atributos da class data
         
    }
    
    
     public void impressaoGeral() {
	 
	    // note que estamos chamando o novo relatório
	    InputStream inputStream = getClass().getResourceAsStream( "/Geral.jasper" );
	 
	    // mapa de parâmetros do relatório
	    Map parametros = new HashMap();
	 
	    /*
	     * Insere o parâmetro primeiroNome no mapa, com o valor F%
	     * ou seja, todos os clientes que tenham primeiro nome começando
	     * com a letra F.
	     */
	   
	    // outros possíveis parâmetros aqui...
	 
	    try {
	 
	        // abre o relatório
	        Impressao.openReport( "Geral", inputStream, parametros,
	                ConexaoImpressao.getConnection() );
                
	    } catch ( SQLException exc ) {
                JOptionPane.showMessageDialog(null,"Erro ao imprimir relatório geral.\n"+exc);
	        exc.printStackTrace();
	    } catch ( JRException exc ) {
                JOptionPane.showMessageDialog(null,"Erro ao imprimir relatório geral.\n"+exc);
	        exc.printStackTrace();
	    }
	 
	}
     
     public void impressaoTurno1() {
	 
	    // note que estamos chamando o novo relatório
	    InputStream inputStream = getClass().getResourceAsStream( "/Turno1.jasper" );
	 
	    // mapa de parâmetros do relatório
	    Map parametros = new HashMap();
	 
	    /*
	     * Insere o parâmetro primeiroNome no mapa, com o valor F%
	     * ou seja, todos os clientes que tenham primeiro nome começando
	     * com a letra F.
	     */
	   
	 
	    // outros possíveis parâmetros aqui...
	 
	    try {
	 
	        // abre o relatório
	        Impressao.openReport( "Turno1", inputStream, parametros,
	                ConexaoImpressao.getConnection() );
               
	    } catch ( SQLException exc ) {
	        exc.printStackTrace();
	    } catch ( JRException exc ) {
	        exc.printStackTrace();
	    }
	 
	}
     
          public void impressaoTurno2() {
	 
	    // note que estamos chamando o novo relatório
	    InputStream inputStream = getClass().getResourceAsStream( "/Turno2.jasper" );
	 
	    // mapa de parâmetros do relatório
	    Map parametros = new HashMap();
	 
	    /*
	     * Insere o parâmetro primeiroNome no mapa, com o valor F%
	     * ou seja, todos os clientes que tenham primeiro nome começando
	     * com a letra F.
	     */
	   
	 
	    // outros possíveis parâmetros aqui...
	 
	    try {
	 
	        // abre o relatório
	        Impressao.openReport( "Turno2", inputStream, parametros,
	                ConexaoImpressao.getConnection() );
                
	    } catch ( SQLException exc ) {
	        exc.printStackTrace();
	    } catch ( JRException exc ) {
	        exc.printStackTrace();
	    }
	 
	}
     
     
    
    
    //MÉTODO PARA VERIFICAS AS CARTEIRAS PRESTES A VENCER
    public void verificaVencimento () throws SQLException{
         DateFormat year = new SimpleDateFormat("yyyy");//formato de data para ano
      Date date = new Date();
      String anoSistema = year.format(date);//Variável que recebe o ano atual do sistema
      
       int mesSistema = date.getMonth()+1;//Variável que recebe o mês atual do sistema
       int mes=0;
      
     
          String sql = "select *from motoristas";//seleciona todos os motoristas no banco de dados
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
         
        while(rs.next()){//Enquanto houver registros, executa a rotina
            
            if(rs.getString("mes_venc").equals("Janeiro")){
                mes = 1;
            }else if(rs.getString("mes_venc").equals("Fevereiro")){
                mes = 2;
            }else if(rs.getString("mes_venc").equals("Março")){
                mes = 3;
            }else if(rs.getString("mes_venc").equals("Abril")){
                mes = 4;
            }else if(rs.getString("mes_venc").equals("Maio")){
                mes = 5;
            }else if(rs.getString("mes_venc").equals("Junho")){
                mes = 6;
            }else if(rs.getString("mes_venc").equals("Julho")){
                mes = 7;
            }else if(rs.getString("mes_venc").equals("Agosto")){
                mes = 8;
            }else if(rs.getString("mes_venc").equals("Setembro")){
                mes = 9;
            }else if(rs.getString("mes_venc").equals("Outubro")){
                mes = 10;
            }else if(rs.getString("mes_venc").equals("Novembro")){
                mes = 11;
            }else if(rs.getString("mes_venc").equals("Dezembro")){
                mes = 12;
            }
            
            int mesvenc = mes - mesSistema;//Subtrai o mês de vencimento da carteira do motorista pelo mês atual do sistema
            int anovenc = Integer.valueOf(rs.getString("ano_venc"))- Integer.valueOf(anoSistema);//Subtrai o ano de vencimento da carteira do motorista pelo ano atual do sistema
            
            
            if(anovenc<0 || anovenc==0 && mesvenc<=1){//Se a diferença entre o ano de vencimento e o ano do sistema for 0, indica que a carteira vence este ano, se a diferença entre o mes de vencimento e o mes atual do sistema for menor ou igual a 1, falta 1 mês para a cateira vencer ou já está vencida
                opvenc = "Sim";//Variável para jogar o valor pro campo opvenc no banco de dados
                
            }else{
                  opvenc = "Não";
            }
            
             atributos at = new atributos();
                 motoristaDao dao =  new motoristaDao();
                 
                 at.setCodigoMotorista(Long.valueOf(rs.getString("cod")));
                 at.setOpVenc(opvenc);//Seta os valores
                 
                 dao.alteraOpVenc(at);//Realiza alteração em opvenc
             
        }
      
        rs.close();
         stmt.close();
         
    }
    
     protected void listarMotoristas() throws SQLException{
        motoristaDao dao = new motoristaDao ();
        opvenc = "Sim";
        motoristas  = dao.getListaOpVenc(opvenc);
        mostraPesquisaMotorista(motoristas);
       
    }
        boolean msg=true;
        private void mostraPesquisaMotorista(List<atributos> consultas) {
      while(tmMotoristas.getRowCount()>0){
          tmMotoristas.removeRow(0);
      }
        if(motoristas.isEmpty()){
            msg=false;
      }else{
            msg=true;
          String []linha = new String [] {null, null, null, null};
          for (int i = 0; i < motoristas.size(); i++){
              tmMotoristas.addRow(linha);
              tmMotoristas.setValueAt(consultas.get(i).getCodigoMotorista(), i, 0);
              tmMotoristas.setValueAt(consultas.get(i).getNome(), i, 1);
              tmMotoristas.setValueAt(consultas.get(i).getCnh(), i, 2);
              tmMotoristas.setValueAt(consultas.get(i).getDtVenc(), i, 3);
          }
      }
    }
    
    //Método que verifica o nível de usuário logado
       public void verificaNivel(atributos at){
           if(at.getNivelUsuario().equals("Comum")){
             jMUsuarios.setVisible(false);
              jMBackup.setVisible(false);
           }else{
                jMBackup.setVisible(true);
               jMUsuarios.setVisible(true);
           }  
           this.setTitle("CaminhoCerto - "+at.getLogin());
       }
       
       //Faz logoff de usuário, retornando à tela de login
    public void logoff() throws SQLException{
        UIManager.put("OptionPane.noButtonText", "Não");  
         UIManager.put("OptionPane.yesButtonText", "Sim");
         int x = JOptionPane.showConfirmDialog(null, "Deseja fazer logoff deste usuário?", "Alerta",0, 2);
        
        if (x==0) {
            this.dispose();
        JFLogin frmlogin = new JFLogin();
        frmlogin.setVisible(true);
        
        }
    }
    
    private void linhaSelecionada(JTable tabela) throws SQLException{
          
        if(jTCarteiras.getSelectedRow()!=-1){
            atributos at = new atributos();
        at.setCodigoMotorista(motoristas.get(tabela.getSelectedRow()).getCodigoMotorista());
        at.setNome(motoristas.get(tabela.getSelectedRow()).getNome());
        at.setDiaNasc(motoristas.get(tabela.getSelectedRow()).getDiaNasc());
        at.setMesNasc(motoristas.get(tabela.getSelectedRow()).getMesNasc());
        at.setAnoNasc(motoristas.get(tabela.getSelectedRow()).getAnoNasc());
        at.setRg(motoristas.get(tabela.getSelectedRow()).getRg());
        at.setCpf(motoristas.get(tabela.getSelectedRow()).getCpf());
        at.setCnh(motoristas.get(tabela.getSelectedRow()).getCnh());
        at.setCat(motoristas.get(tabela.getSelectedRow()).getCat());
        at.setDiaVenc(motoristas.get(tabela.getSelectedRow()).getDiaVenc());
        at.setMesVenc(motoristas.get(tabela.getSelectedRow()).getMesVenc());
        at.setAnoVenc(motoristas.get(tabela.getSelectedRow()).getAnoVenc());
        at.setCarreta(motoristas.get(tabela.getSelectedRow()).getCarreta());
        at.setAnoCarreta(motoristas.get(tabela.getSelectedRow()).getAnoCarreta());
        at.setModelo(motoristas.get(tabela.getSelectedRow()).getModelo());
        at.setPlaca(motoristas.get(tabela.getSelectedRow()).getPlaca());
        at.setAnoPlaca(motoristas.get(tabela.getSelectedRow()).getAnoPlaca());
        at.setCapTon(motoristas.get(tabela.getSelectedRow()).getCapTon());
        at.setTurno(motoristas.get(tabela.getSelectedRow()).getTurno());
        at.setPeriodo(motoristas.get(tabela.getSelectedRow()).getPeriodo());
        at.setTelRes(motoristas.get(tabela.getSelectedRow()).getTelRes());
        at.setTelCom(motoristas.get(tabela.getSelectedRow()).getTelCom());
        at.setCel(motoristas.get(tabela.getSelectedRow()).getCel());
        at.setId(motoristas.get(tabela.getSelectedRow()).getId());
        at.setTelRec(motoristas.get(tabela.getSelectedRow()).getTelRec());
        at.setEmail(motoristas.get(tabela.getSelectedRow()).getEmail());
        at.setCep(motoristas.get(tabela.getSelectedRow()).getCep());
        at.setRua(motoristas.get(tabela.getSelectedRow()).getRua());
        at.setNumero(motoristas.get(tabela.getSelectedRow()).getNumero());
        at.setCompl(motoristas.get(tabela.getSelectedRow()).getCompl());
        at.setBairro(motoristas.get(tabela.getSelectedRow()).getBairro());
        at.setCidade(motoristas.get(tabela.getSelectedRow()).getCidade());
        at.setUf(motoristas.get(tabela.getSelectedRow()).getUf());
        at.setPais(motoristas.get(tabela.getSelectedRow()).getPais());
        
       JDCadastros frm = new JDCadastros(this,true);
        
        frm.recebeDados(at);
        frm.setVisible(true);
        }
    }
    
      //Encerra o programa
     public void sair() throws SQLException{
        UIManager.put("OptionPane.noButtonText", "Não");  
         UIManager.put("OptionPane.yesButtonText", "Sim");
         int x = JOptionPane.showConfirmDialog(null, "Deseja sair do programa?", "Alerta",0, 2);
        
        if (x==0) {
           System.exit(0);
        
        }
    }
    
     
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem4 = new javax.swing.JMenuItem();
        jLLogo = new javax.swing.JLabel();
        jLData = new javax.swing.JLabel();
        jLDt = new javax.swing.JLabel();
        jPAtalhos = new javax.swing.JPanel();
        jLCadastro = new javax.swing.JLabel();
        jLRelTurno2 = new javax.swing.JLabel();
        jLRelTurno1 = new javax.swing.JLabel();
        jLRelGeral = new javax.swing.JLabel();
        jLTNovoMotorista = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLTRelGeral = new javax.swing.JLabel();
        jLTRelTurno1 = new javax.swing.JLabel();
        jLTRelTurno2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTCarteiras = new javax.swing.JTable();
        jLBackground = new javax.swing.JLabel();
        JFC_Backup = new javax.swing.JFileChooser();
        jMenuBar = new javax.swing.JMenuBar();
        jMCaminhoCerto = new javax.swing.JMenu();
        jMAjuda = new javax.swing.JMenuItem();
        jMSobre = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMLogoff = new javax.swing.JMenuItem();
        jMSair = new javax.swing.JMenuItem();
        jMMenu = new javax.swing.JMenu();
        jMCadastros = new javax.swing.JMenu();
        jMMotoristas = new javax.swing.JMenuItem();
        jMUsuarios = new javax.swing.JMenuItem();
        jMRelatorios = new javax.swing.JMenu();
        jMGeral = new javax.swing.JMenuItem();
        jMTurno1 = new javax.swing.JMenuItem();
        jMTurno2 = new javax.swing.JMenuItem();
        jMBackup = new javax.swing.JMenu();
        JB_Backup = new javax.swing.JMenuItem();
        jMRes = new javax.swing.JMenuItem();

        jMenuItem4.setText("jMenuItem4");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        jLLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/logocc.png"))); // NOI18N
        getContentPane().add(jLLogo);
        jLLogo.setBounds(830, 20, 90, 90);

        jLData.setText("Data:");
        getContentPane().add(jLData);
        jLData.setBounds(630, 80, 170, 14);

        jLDt.setText("##/##/####");
        getContentPane().add(jLDt);
        jLDt.setBounds(660, 80, 260, 14);

        jPAtalhos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLCadastro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLCadastro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cd_motorista.png"))); // NOI18N
        jLCadastro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLCadastroMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLCadastroMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLCadastroMouseExited(evt);
            }
        });

        jLRelTurno2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLRelTurno2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Places-folder-yellow-icon (1).png"))); // NOI18N
        jLRelTurno2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLRelTurno2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLRelTurno2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLRelTurno2MouseExited(evt);
            }
        });

        jLRelTurno1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLRelTurno1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Places-folder-violet-icon (1).png"))); // NOI18N
        jLRelTurno1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLRelTurno1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLRelTurno1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLRelTurno1MouseExited(evt);
            }
        });

        jLRelGeral.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLRelGeral.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Places-folder-green-icon (1).png"))); // NOI18N
        jLRelGeral.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLRelGeralMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLRelGeralMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLRelGeralMouseExited(evt);
            }
        });

        jLTNovoMotorista.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTNovoMotorista.setText("Novo Motorista");

        jSeparator1.setForeground(new java.awt.Color(200, 200, 200));

        jLTRelGeral.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTRelGeral.setText("Relatório Geral");

        jLTRelTurno1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTRelTurno1.setText("Relatório Turno 1");

        jLTRelTurno2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTRelTurno2.setText("Relatório Turno 2");

        jSeparator2.setForeground(new java.awt.Color(200, 200, 200));

        jSeparator3.setForeground(new java.awt.Color(200, 200, 200));

        javax.swing.GroupLayout jPAtalhosLayout = new javax.swing.GroupLayout(jPAtalhos);
        jPAtalhos.setLayout(jPAtalhosLayout);
        jPAtalhosLayout.setHorizontalGroup(
            jPAtalhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPAtalhosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPAtalhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addComponent(jLCadastro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLTNovoMotorista, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLRelGeral, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLTRelGeral, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLRelTurno1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLTRelTurno1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
            .addComponent(jLRelTurno2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLTRelTurno2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPAtalhosLayout.setVerticalGroup(
            jPAtalhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPAtalhosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLTNovoMotorista)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLRelGeral, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLTRelGeral)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLRelTurno1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLTRelTurno1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLRelTurno2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLTRelTurno2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPAtalhos);
        jPAtalhos.setBounds(260, 150, 110, 340);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTCarteiras.setModel(tmMotoristas);
        jTCarteiras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTCarteirasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTCarteiras);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(380, 110, 550, 420);

        jLBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/blue_waves2.jpg"))); // NOI18N
        getContentPane().add(jLBackground);
        jLBackground.setBounds(0, 0, 1300, 635);

        JFC_Backup.setCurrentDirectory(new java.io.File("C:\\"));
            JFC_Backup.setDialogTitle("Escolher o Arquivo de Backup");
            JFC_Backup.setFileFilter(null);
            getContentPane().add(JFC_Backup);
            JFC_Backup.setBounds(20, 20, 614, 397);

            jMCaminhoCerto.setText("CaminhoCerto");

            jMAjuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Ajuda.png"))); // NOI18N
            jMAjuda.setText("Ajuda");
            jMCaminhoCerto.add(jMAjuda);

            jMSobre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Sobre.png"))); // NOI18N
            jMSobre.setText("Sobre");
            jMSobre.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMSobreActionPerformed(evt);
                }
            });
            jMCaminhoCerto.add(jMSobre);
            jMCaminhoCerto.add(jSeparator4);

            jMLogoff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/logoff.png"))); // NOI18N
            jMLogoff.setText("Logoff");
            jMLogoff.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMLogoffActionPerformed(evt);
                }
            });
            jMCaminhoCerto.add(jMLogoff);

            jMSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit.png"))); // NOI18N
            jMSair.setText("Sair");
            jMSair.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMSairActionPerformed(evt);
                }
            });
            jMCaminhoCerto.add(jMSair);

            jMenuBar.add(jMCaminhoCerto);

            jMMenu.setText("Menu");

            jMCadastros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/llll.png"))); // NOI18N
            jMCadastros.setText("Cadastros");

            jMMotoristas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/eeee.png"))); // NOI18N
            jMMotoristas.setText("Motoristas");
            jMMotoristas.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMMotoristasActionPerformed(evt);
                }
            });
            jMCadastros.add(jMMotoristas);

            jMUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/user.png"))); // NOI18N
            jMUsuarios.setText("Usuários");
            jMUsuarios.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMUsuariosActionPerformed(evt);
                }
            });
            jMCadastros.add(jMUsuarios);

            jMMenu.add(jMCadastros);

            jMRelatorios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ooo.png"))); // NOI18N
            jMRelatorios.setText("Relatórios");

            jMGeral.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Places-folder-green-icon.png"))); // NOI18N
            jMGeral.setText("Geral");
            jMGeral.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMGeralActionPerformed(evt);
                }
            });
            jMRelatorios.add(jMGeral);

            jMTurno1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Places-folder-violet-icon.png"))); // NOI18N
            jMTurno1.setText("Turno 1");
            jMTurno1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMTurno1ActionPerformed(evt);
                }
            });
            jMRelatorios.add(jMTurno1);

            jMTurno2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Places-folder-yellow-icon.png"))); // NOI18N
            jMTurno2.setText("Turno 2");
            jMTurno2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMTurno2ActionPerformed(evt);
                }
            });
            jMRelatorios.add(jMTurno2);

            jMMenu.add(jMRelatorios);

            jMBackup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bkp.gif"))); // NOI18N
            jMBackup.setText("Backup");

            JB_Backup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/jjj.gif"))); // NOI18N
            JB_Backup.setText("Expotar");
            JB_Backup.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    JB_BackupActionPerformed(evt);
                }
            });
            jMBackup.add(JB_Backup);

            jMRes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/rrr.png"))); // NOI18N
            jMRes.setText("Importar");
            jMRes.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMResActionPerformed(evt);
                }
            });
            jMBackup.add(jMRes);

            jMMenu.add(jMBackup);

            jMenuBar.add(jMMenu);

            setJMenuBar(jMenuBar);

            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            setBounds((screenSize.width-972)/2, (screenSize.height-609)/2, 972, 609);
        }// </editor-fold>//GEN-END:initComponents

    private void jMSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMSobreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMSobreActionPerformed

    private void jMMotoristasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMMotoristasActionPerformed
       JDCadastros frm;
        try {
            frm = new JDCadastros(this, true);
            frm.setVisible(true);
        } catch (SQLException ex) {
           
        }
       
    }//GEN-LAST:event_jMMotoristasActionPerformed

    private void jLCadastroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLCadastroMouseClicked
       JDCadastros frm;
        try {
            frm = new JDCadastros(this, true);
            frm.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao abrir o formulário de cadastro de motoristas.\nErro: "+ex);
        }
       
    }//GEN-LAST:event_jLCadastroMouseClicked

    private void jMUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMUsuariosActionPerformed
        JDUsuarios frm;
        try {
            frm = new JDUsuarios(this, true);
            frm.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro com o banco de dados ao abrir formulário cadastro de usuário.\n" + ex);
        }
        
    }//GEN-LAST:event_jMUsuariosActionPerformed

    private void jMLogoffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMLogoffActionPerformed
          try {
            logoff();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema ao fazer logoff.\n" + ex, "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jMLogoffActionPerformed

    private void jMSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMSairActionPerformed
       try {
            sair();
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null,"Problema ao sair do programa. \n"+ex);
        }       
    }//GEN-LAST:event_jMSairActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            sair();
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null,"Problema ao sair do programa. \n"+ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void jTCarteirasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTCarteirasMouseClicked
       try {
            linhaSelecionada(jTCarteiras);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema ao selecionar motorista.\n" + ex, "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jTCarteirasMouseClicked

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
         try {
            mostraData();
         verificaVencimento();
         listarMotoristas();
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Problema ao atualizar lista de motoristas.\n"+ex,"Erro",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void JB_BackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JB_BackupActionPerformed
         // Botão Backup
        try {
String arquivo = null;

JFC_Backup.setVisible(true);                        

int result = JFC_Backup.showSaveDialog(null);

if(result == JFileChooser.APPROVE_OPTION){
arquivo = JFC_Backup.getSelectedFile().toString().concat(".sql");

File file = new File(arquivo); 

if(file.exists()){
   Object[] options = { "Sim", "Não" };
   
  
                int opcao = JOptionPane.showOptionDialog(null,"Este arquivo já existe. Deseja sobreescrever este arquivo?", "Atenção",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,options, options[0]);
                    if (opcao == JOptionPane.YES_OPTION) {                
                        Runtime bck = Runtime.getRuntime();   
                        bck.exec("C:\\Backup\\mysqldump.exe -v -v -v --host=192.168.0.21 --user=root --password=321 --port=3306 --protocol=tcp --force --allow-keywords --compress  --add-drop-table --default-character-set=latin1 --hex-blob  --result-file="+arquivo+" --databases caminhocerto_bd");  
                        JOptionPane.showMessageDialog(null, "Backup realizado com sucesso.", "Backup", 1);
                    }else{
                        JB_BackupActionPerformed(evt);
                    }
}else{
 
                        Runtime bck = Runtime.getRuntime();   
                        bck.exec("C:\\Backup\\mysqldump.exe -v -v -v --host=192.168.0.21 --user=root --password=321 --port=3306 --protocol=tcp --force --allow-keywords --compress  --add-drop-table --default-character-set=latin1 --hex-blob  --result-file="+arquivo+" --databases caminhocerto_bd");  
                        JOptionPane.showMessageDialog(null, "Backup realizado com sucesso.", "Backup", 1);   
}

}
    
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null,"Erro ao realizar backup.\nErro: "+e, "Erro", 2);
            
        }
    }//GEN-LAST:event_JB_BackupActionPerformed

    private void jMResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMResActionPerformed
       
       try {
   JFC_Backup.setVisible(true);  
               String bd = "caminhocerto_bd";
  int result = JFC_Backup.showOpenDialog(null); 
 
  if(result == JFileChooser.OPEN_DIALOG){
  
                File bkp;  
            bkp = JFC_Backup.getSelectedFile();  
           String arq = bkp.toString();  
                   
String[] cmd = new String[3];
               cmd[0] = "cmd.exe" ;
                cmd[1] = "/C" ;
                 cmd[2] = "c:\\Backup\\mysql --user=root --password=321 < "+arq ;
                 
         Runtime rt = Runtime.getRuntime();
            System.out.println("Execing " + cmd[0] + " " + cmd[1]);
            proc = rt.exec(cmd);
            
                        // Mensagens de erro
            Backup errorGobbler = new 
            Backup(proc.getErrorStream(), "ERROR");            
            
            // Mensagens de saída
            Backup outputGobbler = new 
            Backup(proc.getInputStream(), "OUTPUT");
                
            // kick them off
            errorGobbler.run();
            outputGobbler.run();
                                    
            // Mensagens de erro
            int exitVal = proc.waitFor();
      
       
                        if (exitVal == 0){  
                JOptionPane.showMessageDialog(null, "Backup restaurado com sucesso.","Parabéns",JOptionPane.INFORMATION_MESSAGE);  
            }  
            else{  
                JOptionPane.showMessageDialog(null, "Falha ao restaurar backup. \n Verifique se o caminho selecionado possui espaços.");  
            }
}
           
  
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Erro ao restaurar backup.\nErro: "+e, "Erro", 2);           
        }        
    }//GEN-LAST:event_jMResActionPerformed

    private void jLCadastroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLCadastroMouseEntered

        jLCadastro.setIcon(new ImageIcon(getClass().getResource("/Icons/caminhao2.png")));
        
    }//GEN-LAST:event_jLCadastroMouseEntered

    private void jLCadastroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLCadastroMouseExited
                   
       jLCadastro.setIcon(new ImageIcon(getClass().getResource("/Icons/rss-icon (1).png")));
        
    }//GEN-LAST:event_jLCadastroMouseExited

    private void jLRelGeralMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLRelGeralMouseEntered
        
       jLRelGeral.setIcon(new ImageIcon(getClass().getResource("/Icons/pastaverde2.png")));
    }//GEN-LAST:event_jLRelGeralMouseEntered

    private void jLRelGeralMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLRelGeralMouseExited
        
        jLRelGeral.setIcon(new ImageIcon(getClass().getResource("/Icons/Places-folder-green-icon (1).png")));
        
    }//GEN-LAST:event_jLRelGeralMouseExited

    private void jLRelTurno1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLRelTurno1MouseEntered
        
       jLRelTurno1.setIcon(new ImageIcon(getClass().getResource("/Icons/pastaroxa2.png")));
        
    }//GEN-LAST:event_jLRelTurno1MouseEntered
     
    private void jLRelTurno1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLRelTurno1MouseExited

       jLRelTurno1.setIcon(new ImageIcon(getClass().getResource("/Icons/Places-folder-violet-icon (1).png")));
        
    }//GEN-LAST:event_jLRelTurno1MouseExited

    private void jLRelTurno2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLRelTurno2MouseEntered
       
      jLRelTurno2.setIcon(new ImageIcon(getClass().getResource("/Icons/pastaamarela2.png"))); 
       
    }//GEN-LAST:event_jLRelTurno2MouseEntered

    private void jLRelTurno2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLRelTurno2MouseExited
        
       jLRelTurno2.setIcon(new ImageIcon(getClass().getResource("/Icons/Places-folder-yellow-icon (1).png"))); 
        
    }//GEN-LAST:event_jLRelTurno2MouseExited

    private void jLRelGeralMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLRelGeralMouseClicked
        impressaoGeral();
    }//GEN-LAST:event_jLRelGeralMouseClicked

    private void jLRelTurno1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLRelTurno1MouseClicked
      impressaoTurno1();
    }//GEN-LAST:event_jLRelTurno1MouseClicked

    private void jLRelTurno2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLRelTurno2MouseClicked
      impressaoTurno2();
    }//GEN-LAST:event_jLRelTurno2MouseClicked

    private void jMGeralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMGeralActionPerformed
        impressaoGeral();
    }//GEN-LAST:event_jMGeralActionPerformed

    private void jMTurno1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMTurno1ActionPerformed
        impressaoTurno1();
    }//GEN-LAST:event_jMTurno1ActionPerformed

    private void jMTurno2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMTurno2ActionPerformed
        impressaoTurno2();
    }//GEN-LAST:event_jMTurno2ActionPerformed

    
  
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    new JFPrincipal().setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Problema ao abrir formulário principal.\nErro: "+ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem JB_Backup;
    private javax.swing.JFileChooser JFC_Backup;
    private javax.swing.JLabel jLBackground;
    private javax.swing.JLabel jLCadastro;
    private javax.swing.JLabel jLData;
    private javax.swing.JLabel jLDt;
    private javax.swing.JLabel jLLogo;
    private javax.swing.JLabel jLRelGeral;
    private javax.swing.JLabel jLRelTurno1;
    private javax.swing.JLabel jLRelTurno2;
    private javax.swing.JLabel jLTNovoMotorista;
    private javax.swing.JLabel jLTRelGeral;
    private javax.swing.JLabel jLTRelTurno1;
    private javax.swing.JLabel jLTRelTurno2;
    private javax.swing.JMenuItem jMAjuda;
    private javax.swing.JMenu jMBackup;
    private javax.swing.JMenu jMCadastros;
    private javax.swing.JMenu jMCaminhoCerto;
    private javax.swing.JMenuItem jMGeral;
    private javax.swing.JMenuItem jMLogoff;
    private javax.swing.JMenu jMMenu;
    private javax.swing.JMenuItem jMMotoristas;
    private javax.swing.JMenu jMRelatorios;
    private javax.swing.JMenuItem jMRes;
    private javax.swing.JMenuItem jMSair;
    private javax.swing.JMenuItem jMSobre;
    private javax.swing.JMenuItem jMTurno1;
    private javax.swing.JMenuItem jMTurno2;
    private javax.swing.JMenuItem jMUsuarios;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPAtalhos;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JTable jTCarteiras;
    // End of variables declaration//GEN-END:variables
}
