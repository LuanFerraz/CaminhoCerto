package forms;

import atributos.atributos;
import bancodedados.ConexaoImpressao;
import bancodedados.criarConexao;
import bancodedados.usuarioDao;
import caminhocerto.Impressao;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;

public class JDUsuarios extends javax.swing.JDialog {

      private Connection conexao;
      
       DefaultTableModel tmUsuarios = new DefaultTableModel(null, new String []{"Código", "Nome", "Login", "Tipo", "Senha"});
    List<atributos> usuarios;
    ListSelectionModel lsmUsuarios;
   
    public JDUsuarios(java.awt.Frame parent, boolean modal) throws SQLException {
        super(parent, modal);
        initComponents();
        
        
          this.conexao = criarConexao.getConexao();
        jTNome.requestFocus();
        
         listarUsuarios();//FAZ A PESQUISA DE USUÁRIOS CADASTRADOS AO ABRIR O FORM
         
         //Altera o tamanho das colunas nome e login na tabela de pesquisa
         jTDados.getColumnModel().getColumn(0).setPreferredWidth(60);
         jTDados.getColumnModel().getColumn(1).setPreferredWidth(140);
        jTDados.getColumnModel().getColumn(2).setPreferredWidth(80);
         jTDados.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagens/iconcc.png")));
        
           }
    
    
     public void impressaoGeral() {
	 
	    // note que estamos chamando o novo relatório
	    InputStream inputStream = getClass().getResourceAsStream( "/Geral2.jasper" );
	 
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
	        Impressao.openReport( "Geral2", inputStream, parametros,
	                ConexaoImpressao.getConnection() );
                
	    } catch ( SQLException exc ) {
	        exc.printStackTrace();
	    } catch ( JRException exc ) {
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
    
    //MÉTODOS DO PROGRAMA
    
   //Salva os dados de usuário cadastrado no banco de dados
      public void cadastrarUsuario() throws SQLException{
           if(verificaDados()){
                
          String sql = "select *from usuario";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery();
         boolean igual=false;
         
         while(rs.next()){
             if(jTLogin.getText().equals(rs.getString("login_usuario"))){
                 
                 igual=true; //Verifica se o usuário cadastro já exite
             }
         }
         
         rs.close();
         stmt.close();
         
         if(igual==false){
               try {
            atributos user = new atributos();
            user.setNomeUsuario(jTNome.getText());
            user.setLogin(jTLogin.getText());
            user.setSenha(jPSenha.getText());
            user.setConfirmaSenha(jPConfirma.getText());
            user.setNivelUsuario(jCTipo.getSelectedItem().toString());
            
            usuarioDao dao = new usuarioDao();
            dao.cadastroUsuario(user);
            desabilitaDados();
            listarUsuarios();
            JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso.","Cadastro",JOptionPane.INFORMATION_MESSAGE); 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro com o banco de dados.\n"+ex,"Erro",JOptionPane.ERROR_MESSAGE);
        }
         }else{
             JOptionPane.showMessageDialog(this,"Este usuário já está cadastrado, utilize outro login","Atenção",JOptionPane.INFORMATION_MESSAGE);
         }
              
            }
            
        }
        
        public void excluirUsuario() throws SQLException{
            UIManager.put("OptionPane.noButtonText", "Não");  
         UIManager.put("OptionPane.yesButtonText", "Sim");
         int resp = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este usuário?", "Confirmação",0,3);
         if (resp == 0){
                usuarioDao dao;
                dao = new usuarioDao();
                dao.excluirUsuario(usuarios.get(jTDados.getSelectedRow()));
               mostraPesquisa(usuarios);
                JOptionPane.showMessageDialog(null,"Usuário excluído com sucesso.","Excluir",JOptionPane.INFORMATION_MESSAGE);
                limparDados();
            desabilitaDados();
            listarUsuarios();
         }
        }
        
        String loginigual="";
         private void alteraUsuario() throws SQLException {
        if(jTDados.getSelectedRow() != -1){
            UIManager.put("OptionPane.noButtonText", "Não");  
         UIManager.put("OptionPane.yesButtonText", "Sim");
            int resp = JOptionPane.showConfirmDialog(this, "Deseja realmente alterar este usuário?", "Confirmação",0,3);
         if (resp == 0){
             if(verificaDados()){
                  
                  String sql = "select *from usuario";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery();
         boolean login_igual=false;
       
          //Se o usuário tentar alterar o nome ou o cpf para algum já cadastrado o programa irá impedir
         if(!jTLogin.getText().equals(loginigual)){
             while(rs.next()){
             if(jTLogin.getText().equals(rs.getString("login_usuario"))){
                 login_igual=true; 
             }
         }
         }
         
         rs.close();
         stmt.close();
         
         if(login_igual==false){
              atributos at = new atributos();
                usuarioDao dao =  new usuarioDao();
                at.setCodigoUsuario(Long.valueOf(jTCodigo.getText()));
                at.setNomeUsuario(jTNome.getText());
                at.setLogin(jTLogin.getText());
                at.setSenha(jPSenha.getText());
                at.setConfirmaSenha(jPConfirma.getText());
                at.setNivelUsuario((jCTipo.getSelectedItem().toString()));
                dao.alteraUsuario(at);
                 JOptionPane.showMessageDialog(null,"Usuário alterado com sucesso.","Alterar",JOptionPane.INFORMATION_MESSAGE);
                  desabilitaDados();  
                 listarUsuarios();
         }else{
             JOptionPane.showMessageDialog(null,"Este login já está sendo utilizado por outro usuário.","Erro",JOptionPane.WARNING_MESSAGE);
         }
            }
        }
         }
           
         }
         
         
         //Seleciona o tipo de busca e chama os métodos de pesquisa
        protected void listarUsuarios() throws SQLException{
        usuarioDao dao = new usuarioDao ();
        
        switch(jCBusca.getSelectedIndex()){
            case 0:
                 usuarios = dao.getListaLogin("%" + jTBusca.getText() + "%");
        mostraPesquisa(usuarios);
                 
                break;
              
            case 1:
                usuarios = dao.getListaNome("%"+jTBusca.getText()+"%");
        mostraPesquisa(usuarios);
                break;
      
            case 2:
                usuarios = dao.getListaCodigo(jTBusca.getText());
        mostraPesquisa(usuarios);
                break;  
            case 3:
                  usuarios = dao.getListaTipo("%" + jTBusca.getText() + "%");
        mostraPesquisa(usuarios);
           break;
        }
            
    }
        
    boolean msg = true;//Variável usada para guardar mensagem se a pesquisa não encontrar dados
    
    //Efetua pesquisa
      private void mostraPesquisa(List<atributos> usuarios) {
      while(tmUsuarios.getRowCount()>0){
          tmUsuarios.removeRow(0);
      }
        if(usuarios.isEmpty()){
            msg=false;
         
      }else{
            msg=true;
          String []linha = new String [] {null, null, null, null};
          for (int i = 0; i < usuarios.size(); i++){
              tmUsuarios.addRow(linha);
              tmUsuarios.setValueAt(usuarios.get(i).getCodigoUsuario(), i, 0);
              tmUsuarios.setValueAt(usuarios.get(i).getNomeUsuario(), i, 1);
              tmUsuarios.setValueAt(usuarios.get(i).getLogin(), i, 2);
              tmUsuarios.setValueAt(usuarios.get(i).getNivelUsuario(), i, 3);
              tmUsuarios.setValueAt(usuarios.get(i).getSenha(), i, 4);
          }
      }
    }
     
      private void linhaSelecionada(JTable tabela){
          
        if(jTDados.getSelectedRow()!=-1){
            habilitaDados();
            jBGravar.setEnabled(false);
            jBAlterar.setEnabled(true);
            jBImprimir.setEnabled(true);
            jBExcluir.setEnabled(true);
        jTCodigo.setText(String.valueOf(usuarios.get(tabela.getSelectedRow()).getCodigoUsuario()));
        jTNome.setText(usuarios.get(tabela.getSelectedRow()).getNomeUsuario());
        jTLogin.setText(usuarios.get(tabela.getSelectedRow()).getLogin());
        jPSenha.setText(usuarios.get(tabela.getSelectedRow()).getSenha());
        jPConfirma.setText(usuarios.get(tabela.getSelectedRow()).getConfirmaSenha());
        jCTipo.setSelectedItem(usuarios.get(tabela.getSelectedRow()).getNivelUsuario());
        loginigual=jTLogin.getText();
        nome = jTNome.getText();
        login = jTLogin.getText();
        senha = jPSenha.getText();
        confirma = jPConfirma.getText();
        jTUsuarios.setSelectedComponent(jPCadastro);
        } else{
            jTCodigo.setText("");
            jTNome.setText("");
            jTLogin.setText("");
            jPSenha.setText("");
            jPConfirma.setText("");
            jCTipo.setSelectedIndex(0);
       }
      
       jTBusca.setText("");
    }
      
        public void lowerCase(){
             String loginLower="";
             loginLower= jTLogin.getText();
             jTLogin.setText(loginLower.toLowerCase());
         }
        
        public void impressaoUsuario() {
	 
	    // note que estamos chamando o novo relatório
	    InputStream inputStream = getClass().getResourceAsStream( "/Usuarios.jasper" );
	 
	    // mapa de parâmetros do relatório
	    Map parametros = new HashMap();
	 
	    /*
	     * Insere o parâmetro primeiroNome no mapa, com o valor F%
	     * ou seja, todos os clientes que tenham primeiro nome começando
	     * com a letra F.
	     */
	    parametros.put( "codigoUsuario", jTCodigo.getText() );
	 
	    // outros possíveis parâmetros aqui...
	 
	    try {
	 
	        // abre o relatório
	        Impressao.openReport( "Usuarios", inputStream, parametros,
	                ConexaoImpressao.getConnection() );
                
	  this.dispose();
	    } catch ( SQLException exc ) {
	        exc.printStackTrace();
	    } catch ( JRException exc ) {
	        exc.printStackTrace();
	    }
	 
	}


        //Desabilita os campos para edição
        public void desabilitaDados(){
            jTCodigo.setEditable(false);
            jTNome.setEditable(false);
            jTLogin.setEditable(false);
            jPSenha.setEditable(false);
            jPConfirma.setEditable(false);
            jCTipo.setEnabled(false);
            jBGravar.setEnabled(false);
             jBExcluir.setEnabled(false);
            jBCancelar.setEnabled(false);
           jBImprimir.setEnabled(false);
           jBAlterar.setEnabled(false);
           jBNovo.setEnabled(true);
        }
        
        //Habilita os campos para edição
        public void habilitaDados(){
            jTNome.setEditable(true);
            jTLogin.setEditable(true);
            jPSenha.setEditable(true);
            jPConfirma.setEditable(true);
            jCTipo.setEnabled(true);
            jBGravar.setEnabled(true);
           jBCancelar.setEnabled(true);
           jBNovo.setEnabled(false);
        }
        
        //Limpa todos os campos, volta a cor dos marcadores de campos obrigatórios para preto e zera as variáveis de controle de caracteres
        public void limparDados(){
            jTCodigo.setText("");
            jTNome.setText("");
            jTLogin.setText("");
            jPSenha.setText("");
            jPConfirma.setText("");
            jCTipo.setSelectedIndex(0);
             nome = "";
            login = "";
            senha = "";
            confirma="";
            jLObrNome.setForeground(Color.BLACK);
            jLObrLogin.setForeground(Color.BLACK);
            jLObrSenha.setForeground(Color.BLACK);
            jLObrConfirma.setForeground(Color.BLACK);
            jLObrTipo.setForeground(Color.BLACK);
            
        }
        
         //Verifica se todos os campos obrigatórios foram preenchidos
       public boolean verificaDados(){
        if(jTNome.getText().equals("") || jTLogin.getText().equals("") || jPSenha.getText().equals("") 
                || jPConfirma.getText().equals("") || jCTipo.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(null,"Todos os campos devem ser preenchidos.","Erro", JOptionPane.WARNING_MESSAGE);
            if(jCTipo.getSelectedIndex()==0){
                jLObrTipo.setForeground(Color.red);
                jCTipo.requestFocus();
            }else{
                jLObrTipo.setForeground(Color.black);
            }
            
             if(jPConfirma.getText().equals("")){
                jLObrConfirma.setForeground(Color.red);
                jPConfirma.requestFocus();
            }else{
                  jLObrConfirma.setForeground(Color.black);
             }
             
             if(jPSenha.getText().equals("")){
                jLObrSenha.setForeground(Color.red);
                jPSenha.requestFocus();
            }else{
                  jLObrSenha.setForeground(Color.black);
             }
             
             if(jTLogin.getText().equals("")){
                jLObrLogin.setForeground(Color.red);
                jTLogin.requestFocus();
            }else{
                 jLObrLogin.setForeground(Color.black);
             }
             
             if(jTNome.getText().equals("")){
                jLObrNome.setForeground(Color.red);
                jTNome.requestFocus();
            }else{
                 jLObrNome.setForeground(Color.black);
             }
             
            
        return false;
        } else if(!jPConfirma.getText().equals(jPSenha.getText())) {
           JOptionPane.showMessageDialog(null,"Campo confirme a senha diferente da senha.","Erro", JOptionPane.WARNING_MESSAGE);
           jLObrNome.setForeground(Color.BLACK);
            jLObrLogin.setForeground(Color.BLACK);
            jLObrSenha.setForeground(Color.red);
            jLObrConfirma.setForeground(Color.red);
            jLObrTipo.setForeground(Color.BLACK);
        return false;
        }else{
            jLObrNome.setForeground(Color.BLACK);
            jLObrLogin.setForeground(Color.BLACK);
            jLObrSenha.setForeground(Color.BLACK);
            jLObrConfirma.setForeground(Color.BLACK);
            jLObrTipo.setForeground(Color.BLACK);
            return true;
        }
        
    }
       
        public void sair(){
           UIManager.put("OptionPane.noButtonText", "Não");  
         UIManager.put("OptionPane.yesButtonText", "Sim");
         int x = JOptionPane.showConfirmDialog(null, "Deseja encerrar o cadastro de usuários?", "Alerta",0, 2);
        
        if (x==0) {
        this.dispose();
        }
    }
          
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTUsuarios = new javax.swing.JTabbedPane();
        jPCadastro = new javax.swing.JPanel();
        jPDados = new javax.swing.JPanel();
        jLCod = new javax.swing.JLabel();
        jLNome = new javax.swing.JLabel();
        jLLogin = new javax.swing.JLabel();
        jLSenha = new javax.swing.JLabel();
        jLConfirmaSenha = new javax.swing.JLabel();
        jLTipo = new javax.swing.JLabel();
        jPConfirma = new javax.swing.JPasswordField();
        jCTipo = new javax.swing.JComboBox();
        jPSenha = new javax.swing.JPasswordField();
        jTLogin = new javax.swing.JTextField();
        jTNome = new javax.swing.JTextField();
        jTCodigo = new javax.swing.JTextField();
        jLObrNome = new javax.swing.JLabel();
        jLObrLogin = new javax.swing.JLabel();
        jLObrSenha = new javax.swing.JLabel();
        jLObrConfirma = new javax.swing.JLabel();
        jLObrTipo = new javax.swing.JLabel();
        jLCamposObrigatorios = new javax.swing.JLabel();
        jPPesquisa = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTDados = new javax.swing.JTable();
        jPBusca = new javax.swing.JPanel();
        jLBusca = new javax.swing.JLabel();
        jCBusca = new javax.swing.JComboBox();
        jTBusca = new javax.swing.JTextField();
        jPBotoes = new javax.swing.JPanel();
        jBAlterar = new javax.swing.JButton();
        jBExcluir = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jBNovo = new javax.swing.JButton();
        jBGravar = new javax.swing.JButton();
        jBPesquisar = new javax.swing.JButton();
        jBImprimir = new javax.swing.JButton();
        jLBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Cadastro de Usuários");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        jPDados.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPDados.setForeground(new java.awt.Color(0, 51, 102));

        jLCod.setText("Código:");

        jLNome.setText("Nome:");

        jLLogin.setText("Login:");

        jLSenha.setText("Senha:");

        jLConfirmaSenha.setText("Confirme a Senha:");

        jLTipo.setText("Tipo de Usuário:");

        jPConfirma.setEditable(false);
        jPConfirma.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPConfirmaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPConfirmaKeyReleased(evt);
            }
        });

        jCTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Escolha o tipo", "Administrador", "Comum" }));
        jCTipo.setEnabled(false);
        jCTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCTipoKeyPressed(evt);
            }
        });

        jPSenha.setEditable(false);
        jPSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPSenhaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPSenhaKeyReleased(evt);
            }
        });

        jTLogin.setEditable(false);
        jTLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTLoginKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTLoginKeyReleased(evt);
            }
        });

        jTNome.setEditable(false);
        jTNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTNomeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTNomeKeyReleased(evt);
            }
        });

        jTCodigo.setEditable(false);

        jLObrNome.setText("(*)");

        jLObrLogin.setText("(*)");

        jLObrSenha.setText("(*)");

        jLObrConfirma.setText("(*)");

        jLObrTipo.setText("(*)");

        jLCamposObrigatorios.setText("(*) - Campos Obrigatórios");

        javax.swing.GroupLayout jPDadosLayout = new javax.swing.GroupLayout(jPDados);
        jPDados.setLayout(jPDadosLayout);
        jPDadosLayout.setHorizontalGroup(
            jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPDadosLayout.createSequentialGroup()
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPDadosLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPDadosLayout.createSequentialGroup()
                                    .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPDadosLayout.createSequentialGroup()
                                            .addComponent(jLObrNome)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLNome))
                                        .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPDadosLayout.createSequentialGroup()
                                                .addGap(63, 63, 63)
                                                .addComponent(jLObrSenha)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLSenha))
                                            .addGroup(jPDadosLayout.createSequentialGroup()
                                                .addGap(68, 68, 68)
                                                .addComponent(jLObrLogin)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLLogin)))
                                        .addComponent(jLCod))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTNome, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPDadosLayout.createSequentialGroup()
                                    .addComponent(jLObrTipo)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLTipo)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jCTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(84, 84, 84)))
                            .addGroup(jPDadosLayout.createSequentialGroup()
                                .addComponent(jLObrConfirma)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLConfirmaSenha)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPConfirma, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPDadosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLCamposObrigatorios)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPDadosLayout.setVerticalGroup(
            jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPDadosLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLCod)
                    .addComponent(jTCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLNome)
                    .addComponent(jLObrNome)
                    .addComponent(jTNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLLogin)
                    .addComponent(jTLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLObrLogin))
                .addGap(6, 6, 6)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLSenha)
                    .addComponent(jLObrSenha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPConfirma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLConfirmaSenha)
                    .addComponent(jLObrConfirma))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLTipo)
                    .addComponent(jLObrTipo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addComponent(jLCamposObrigatorios)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPCadastroLayout = new javax.swing.GroupLayout(jPCadastro);
        jPCadastro.setLayout(jPCadastroLayout);
        jPCadastroLayout.setHorizontalGroup(
            jPCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPCadastroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPDados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(28, 28, 28))
        );
        jPCadastroLayout.setVerticalGroup(
            jPCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPCadastroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPDados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTUsuarios.addTab("Cadastro", jPCadastro);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTDados.setModel(tmUsuarios);
        jTDados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTDadosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTDados);

        jPBusca.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLBusca.setText("Busca por:");

        jCBusca.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Login", "Nome", "Código", "Tipo" }));

        jTBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTBuscaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPBuscaLayout = new javax.swing.GroupLayout(jPBusca);
        jPBusca.setLayout(jPBuscaLayout);
        jPBuscaLayout.setHorizontalGroup(
            jPBuscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPBuscaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLBusca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTBusca, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPBuscaLayout.setVerticalGroup(
            jPBuscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPBuscaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPBuscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLBusca)
                    .addComponent(jCBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPBusca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPPesquisaLayout = new javax.swing.GroupLayout(jPPesquisa);
        jPPesquisa.setLayout(jPPesquisaLayout);
        jPPesquisaLayout.setHorizontalGroup(
            jPPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPesquisaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPPesquisaLayout.setVerticalGroup(
            jPPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPesquisaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTUsuarios.addTab("Pesquisa", jPPesquisa);

        getContentPane().add(jTUsuarios);
        jTUsuarios.setBounds(30, 20, 500, 390);

        jPBotoes.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPBotoes.setForeground(new java.awt.Color(204, 204, 204));

        jBAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pencil (25x25).png"))); // NOI18N
        jBAlterar.setText("Alterar");
        jBAlterar.setEnabled(false);
        jBAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAlterarActionPerformed(evt);
            }
        });

        jBExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lixo (25x25).png"))); // NOI18N
        jBExcluir.setText("Excluir");
        jBExcluir.setEnabled(false);
        jBExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBExcluirActionPerformed(evt);
            }
        });

        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancel.png"))); // NOI18N
        jBCancelar.setText("Cancelar");
        jBCancelar.setEnabled(false);
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });

        jBNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add (25x25).png"))); // NOI18N
        jBNovo.setText("Novo");
        jBNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBNovoActionPerformed(evt);
            }
        });

        jBGravar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/disk (25x25).png"))); // NOI18N
        jBGravar.setText("Gravar");
        jBGravar.setEnabled(false);
        jBGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGravarActionPerformed(evt);
            }
        });

        jBPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Start-Menu-Search-icon (1).png"))); // NOI18N
        jBPesquisar.setText("Pesquisar");
        jBPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPesquisarActionPerformed(evt);
            }
        });

        jBImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/print-icon.png"))); // NOI18N
        jBImprimir.setText("Imprimir");
        jBImprimir.setEnabled(false);
        jBImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPBotoesLayout = new javax.swing.GroupLayout(jPBotoes);
        jPBotoes.setLayout(jPBotoesLayout);
        jPBotoesLayout.setHorizontalGroup(
            jPBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addComponent(jBGravar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPBotoesLayout.setVerticalGroup(
            jPBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jBNovo, javax.swing.GroupLayout.DEFAULT_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jBGravar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPBotoes);
        jPBotoes.setBounds(560, 70, 135, 305);

        jLBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/blue_waves2.jpg"))); // NOI18N
        getContentPane().add(jLBackground);
        jLBackground.setBounds(0, 0, 770, 450);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-738)/2, (screenSize.height-459)/2, 738, 459);
    }// </editor-fold>//GEN-END:initComponents

    private void jBGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGravarActionPerformed
         lowerCase();
        try {
            cadastrarUsuario();

        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Problema no botão salvar.\n"+ex);
        }
    }//GEN-LAST:event_jBGravarActionPerformed

    private void jTDadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTDadosMouseClicked
       linhaSelecionada(jTDados);
    }//GEN-LAST:event_jTDadosMouseClicked

    private void jBExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBExcluirActionPerformed
       try {
            excluirUsuario();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro no botão Excluir.\n" + ex);
        }
    }//GEN-LAST:event_jBExcluirActionPerformed

    private void jBAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAlterarActionPerformed
      try {
            alteraUsuario();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema no botão alterar.\n"+ex);
        }
    }//GEN-LAST:event_jBAlterarActionPerformed

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
      limparDados();
        desabilitaDados();
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void jBNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNovoActionPerformed

        habilitaDados();
        limparDados();
        jBAlterar.setEnabled(false);
        jTNome.requestFocus();
    }//GEN-LAST:event_jBNovoActionPerformed

    private void jBPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPesquisarActionPerformed
       jTUsuarios.setSelectedComponent(jPPesquisa);
    }//GEN-LAST:event_jBPesquisarActionPerformed

    private void jTBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTBuscaKeyReleased
        try {
                listarUsuarios();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Problemas no campo de pesquisa.\n" + ex);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nenhhum usuário encontrado.\n" + e);
            }
            jTBusca.requestFocus();
    }//GEN-LAST:event_jTBuscaKeyReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        sair();
    }//GEN-LAST:event_formWindowClosing
  String nome="";String login="";String senha = "";String confirma ="";//Variáveis para fazer bloqueio de caracteres digitados
    private void jTNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTNomeKeyPressed
         if (evt.getKeyCode() == 10) {
            jTLogin.requestFocus();
        }//Se o usuário aperta enter, pula para o próximo campo

        if (jTNome.getText().length() < 80) {

            nome = jTNome.getText();

        }

        if (jTNome.getText().length() > 79) {
            if (evt.getKeyCode() != 10) {
                jTNome.setText("");
                jTNome.setText(nome);
            }//Se o nome possuir mais que 79 caracteres o usuário não pode digitar mais

        }
        
        if (evt.getKeyCode() == 38) {
            jCTipo.requestFocus();
        }
    }//GEN-LAST:event_jTNomeKeyPressed

    private void jTNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTNomeKeyReleased
        if (jTNome.getText().length() < 80) {


            nome = jTNome.getText();

        }

        if (jTNome.getText().length() > 79) {
            if (evt.getKeyCode() != 10) {
                jTNome.setText("");
                jTNome.setText(nome);
            }//Se o nome possuir mais que 79 caracteres o usuário não pode digitar mais

        }
    }//GEN-LAST:event_jTNomeKeyReleased

    private void jTLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTLoginKeyPressed
       if (evt.getKeyCode() == 10) {
            jPSenha.requestFocus();
        }//Se o usuário aperta enter, pula para o próximo campo

        if (jTLogin.getText().length() < 19) {

            login = jTLogin.getText();

        }

        if (jTLogin.getText().length() > 18) {
            if (evt.getKeyCode() != 10) {
                jTLogin.setText("");
                jTLogin.setText(login);
            }

        }//O login não pode ultrapassar 18 caracteres

        if (evt.getKeyCode() == 38) {
            jTNome.requestFocus();
        }//Se o usuário apertar a tecla para cima voltará um campo
    }//GEN-LAST:event_jTLoginKeyPressed

    private void jTLoginKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTLoginKeyReleased
          if (jTLogin.getText().length() < 19) {

            login = jTLogin.getText();

        }

        if (jTLogin.getText().length() > 18) {
            if (evt.getKeyCode() != 10) {
                jTLogin.setText("");
                jTLogin.setText(login);
            }

        }//O login não pode ultrapassar 18 caracteres
    }//GEN-LAST:event_jTLoginKeyReleased

    private void jPSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPSenhaKeyPressed
       if (evt.getKeyCode() == 10) {
            jPConfirma.requestFocus();
        }

        if (jPSenha.getText().length() < 9) {

            senha = jPSenha.getText();

        }

        if (jPSenha.getText().length() > 8) {
            if (evt.getKeyCode() != 10) {
                jPSenha.setText("");
                jPSenha.setText(senha);
            }

        }

        if (evt.getKeyCode() == 38) {
            jTLogin.requestFocus();
        }
    }//GEN-LAST:event_jPSenhaKeyPressed

    private void jPSenhaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPSenhaKeyReleased
       if (jPSenha.getText().length() < 9) {

            senha = jPSenha.getText();

        }

        if (jPSenha.getText().length() > 8) {
            if (evt.getKeyCode() != 10) {
                jPSenha.setText("");
                jPSenha.setText(senha);
            }

        }
    }//GEN-LAST:event_jPSenhaKeyReleased

    private void jPConfirmaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPConfirmaKeyPressed
       if (evt.getKeyCode() == 10) {
            jCTipo.requestFocus();
        }

        if (jPConfirma.getText().length() < 9) {

            confirma = jPConfirma.getText();

        }

        if (jPConfirma.getText().length() > 8) {
            if (evt.getKeyCode() != 10) {
                jPConfirma.setText("");
                jPConfirma.setText(confirma);
            }

        }
        if (evt.getKeyCode() == 38) {
            jPSenha.requestFocus();
        }
    }//GEN-LAST:event_jPConfirmaKeyPressed

    private void jPConfirmaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPConfirmaKeyReleased
        if (jPConfirma.getText().length() < 9) {

            confirma = jPConfirma.getText();

        }

        if (jPConfirma.getText().length() > 8) {
            if (evt.getKeyCode() != 10) {
                jPConfirma.setText("");
                jPConfirma.setText(confirma);
            }

        }
    }//GEN-LAST:event_jPConfirmaKeyReleased

    private void jCTipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCTipoKeyPressed
        if (evt.getKeyCode() == 38) {
            jPConfirma.requestFocus();
        }
    }//GEN-LAST:event_jCTipoKeyPressed

    private void jBImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBImprimirActionPerformed
         
        
        
        if (verificaDados()) {
            impressaoUsuario();
        } else {
          JOptionPane.showMessageDialog(null, "Todos os campos obrigatórios devem ser preenchidos para impressão.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
        }    }//GEN-LAST:event_jBImprimirActionPerformed

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
            java.util.logging.Logger.getLogger(JDUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                JDUsuarios dialog = null;
                try {
                    dialog = new JDUsuarios(new javax.swing.JFrame(), true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Problema ao abrir formulário de Cadastro de Usuários.\n"+ex);
                }
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAlterar;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBExcluir;
    private javax.swing.JButton jBGravar;
    private javax.swing.JButton jBImprimir;
    private javax.swing.JButton jBNovo;
    private javax.swing.JButton jBPesquisar;
    private javax.swing.JComboBox jCBusca;
    private javax.swing.JComboBox jCTipo;
    private javax.swing.JLabel jLBackground;
    private javax.swing.JLabel jLBusca;
    private javax.swing.JLabel jLCamposObrigatorios;
    private javax.swing.JLabel jLCod;
    private javax.swing.JLabel jLConfirmaSenha;
    private javax.swing.JLabel jLLogin;
    private javax.swing.JLabel jLNome;
    private javax.swing.JLabel jLObrConfirma;
    private javax.swing.JLabel jLObrLogin;
    private javax.swing.JLabel jLObrNome;
    private javax.swing.JLabel jLObrSenha;
    private javax.swing.JLabel jLObrTipo;
    private javax.swing.JLabel jLSenha;
    private javax.swing.JLabel jLTipo;
    private javax.swing.JPanel jPBotoes;
    private javax.swing.JPanel jPBusca;
    private javax.swing.JPanel jPCadastro;
    private javax.swing.JPasswordField jPConfirma;
    private javax.swing.JPanel jPDados;
    private javax.swing.JPanel jPPesquisa;
    private javax.swing.JPasswordField jPSenha;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTBusca;
    private javax.swing.JTextField jTCodigo;
    private javax.swing.JTable jTDados;
    private javax.swing.JTextField jTLogin;
    private javax.swing.JTextField jTNome;
    private javax.swing.JTabbedPane jTUsuarios;
    // End of variables declaration//GEN-END:variables
}
