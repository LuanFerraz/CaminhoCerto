package forms;

import atributos.atributos;
import bancodedados.ConexaoImpressao;
import bancodedados.criarConexao;
import bancodedados.motoristaDao;
import caminhocerto.Impressao;
import java.awt.Color;
import java.awt.Toolkit;
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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;


public class JDCadastros extends javax.swing.JDialog {

    
    private Connection conexao;
    
    DefaultTableModel tmMotoristas = new DefaultTableModel(null, new String []{"Motorista","Turno","Placa","Carreta","Modelo","CNH","Vencimento"});
    List<atributos> motoristas;
    ListSelectionModel lsmMotoristas;
    
    
   String  nomeigual,cpfigual,cnhigual,carreta, modelo, placa, capton, cnh, cpf, tel, telCom, cel, id ,telRec,cep
        ,nome,rg,email,numero,rua,compl,bairro,cidade,pais;//Variáveis para controle de quantidade de caracteres digitados e bloqueio de 
//letras em campos númericos
    /**
     * Creates new form JDCadastros
     */
    public JDCadastros(java.awt.Frame parent, boolean modal) throws SQLException {
        super(parent, modal);
        initComponents();
        
         //Coencta ao banco de dados
        this.conexao = criarConexao.getConexao();
        
         jLValidacao.setVisible(false);
        jLDtInvalida.setVisible(false);
        jLDtInvalida1.setVisible(false);
        
        //Inicializa os anos no ComboBox da data de nascimento
           DateFormat dataa = new SimpleDateFormat("yyyy");
      Date date = new Date();
      String data = dataa.format(date);
          for(int i=Integer.valueOf(data);i>=1930;i--){
                
                  jCAnoCar.addItem(String.valueOf(i));
                  jCAnoPlaca.addItem(String.valueOf(i));
              }
          
            for(int i=Integer.valueOf(data)-18;i>=1910;i--){
                  jCAnoNasc.addItem(String.valueOf(i));
              }
          
            for(int i=2006;i<=2020;i++){
                  jCAno.addItem(String.valueOf(i));
              }
          listarMotoristas();
          
        
         jTPesquisa.getColumnModel().getColumn(0).setPreferredWidth(215);
         jTPesquisa.getColumnModel().getColumn(1).setPreferredWidth(25);
         jTPesquisa.getColumnModel().getColumn(2).setPreferredWidth(45);
         jTPesquisa.getColumnModel().getColumn(3).setPreferredWidth(40);
         jTPesquisa.getColumnModel().getColumn(4).setPreferredWidth(60);
         jTPesquisa.getColumnModel().getColumn(5).setPreferredWidth(60);
         jTPesquisa.getColumnModel().getColumn(6).setPreferredWidth(90);
        
       this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagens/iconcc.png")));
    }
    

    
    
    
    
     public void cadastrarMotorista() throws SQLException{
            String dataNasc="";
            String dataVenc="";
            String opVenc="";
            if(verificaDados()){
                
                
                String sql = "select *from motoristas";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery();
         boolean igual=false;
        
         //Verifica se o nome ou cpf usado já está cadastrado no sistema
         while(rs.next()){
             if(jTNome.getText().equals(rs.getString("nome"))||
                     jTCPF.getText().equals(rs.getString("cpf")) || jTCNH.getText().equals(rs.getString("cnh"))){
                 igual=true; 
             }
         }
         
         rs.close();
         stmt.close();
         
         if(igual==false){
                  try {
            atributos at = new atributos();
             motoristaDao dao = new motoristaDao();
             
            at.setNome(jTNome.getText());
            at.setDiaNasc(jCDiaNasc.getSelectedItem().toString());
            at.setMesNasc(jCMesNasc.getSelectedItem().toString());
            at.setAnoNasc(jCAnoNasc.getSelectedItem().toString());
                dataNasc = jCDiaNasc.getSelectedItem().toString();
      dataNasc = dataNasc+"/"+jCMesNasc.getSelectedItem().toString();
        dataNasc = dataNasc+"/"+jCAnoNasc.getSelectedItem();toString();
                at.setDtNasc(dataNasc);
            at.setRg(jTRG.getText());
            at.setCpf(jTCPF.getText());
            at.setCnh(jTCNH.getText());
            at.setCat(jCCat.getSelectedItem().toString());
           at.setDiaVenc(jCDia.getSelectedItem().toString());
            at.setMesVenc(jCMes.getSelectedItem().toString());
            at.setAnoVenc(jCAno.getSelectedItem().toString());
                dataVenc = jCDia.getSelectedItem().toString();
      dataVenc = dataVenc+"/"+jCMes.getSelectedItem().toString();
        dataVenc = dataVenc+"/"+jCAno.getSelectedItem();toString();
                at.setDtVenc(dataVenc);
                
                DateFormat year = new SimpleDateFormat("yyyy");//formato de data para ano
      Date date = new Date();
      String anoSistema = year.format(date);//Variável que recebe o ano atual do sistema
      
       int mes = date.getMonth()+1;//Variável que recebe o mês atual do sistema
                
       
       int mesvenc = Integer.valueOf(jCMes.getSelectedIndex())- mes;//Subtrai o mês de vencimento da carteira do motorista pelo mês atual do sistema
            int anovenc = Integer.valueOf(jCAno.getSelectedItem().toString())- Integer.valueOf(anoSistema);//Subtrai o ano de vencimento da carteira do motorista pelo ano atual do sistema
            
            if(anovenc<0 || anovenc==0 && mesvenc<=1){//Se a diferença entre o ano de vencimento e o ano do sistema for 0, indica que a carteira vence este ano, se a diferença entre o mes de vencimento e o mes atual do sistema for menor ou igual a 1, falta 1 mês para a cateira vencer ou já está vencida
                opVenc = "Sim";//Variável para jogar o valor pro campo opvenc no banco de dados
            }else{
                opVenc = "Não";
            }
                at.setOpVenc(opVenc);
             at.setCarreta(jTCarreta.getText());
             at.setAnoCarreta(jCAnoCar.getSelectedItem().toString());
             at.setModelo(jTModelo.getText());
             at.setPlaca(jTPlaca.getText());
             at.setAnoPlaca(jCAnoPlaca.getSelectedItem().toString());
             at.setCapTon(jTCap.getText());
             at.setTurno(jCTurno.getSelectedItem().toString());
             at.setPeriodo(jCPeriodo.getSelectedItem().toString());
            at.setTelRes(jFTelRes.getText());
                at.setTelCom(jFTelCom.getText());
                at.setCel(jFCel.getText());
                at.setId(jFId.getText());
                at.setTelRec(jFTelRec.getText());
                at.setEmail(jTEmail.getText());
                at.setRua(jTRua.getText());
                at.setNumero(jTNumero.getText());
                at.setCompl(jTComplemento.getText());
                at.setCep(jFTCep.getText());
                at.setBairro(jTBairro.getText());
                at.setCidade(jTCidade.getText());
                at.setUf(jCEstado.getSelectedItem().toString());
                at.setPais(jTPais.getText());
           
            dao.cadastroMotorista(at);
                   
            desabilitaDados();
            listarMotoristas();
            JOptionPane.showMessageDialog(null, "Motorista cadastrado com sucesso.","Cadastro",JOptionPane.INFORMATION_MESSAGE); 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro com o banco de dados.\n"+ex,"Erro",JOptionPane.ERROR_MESSAGE);
        }
         }else{
             JOptionPane.showMessageDialog(this, "Este nome, CPF ou CNH já está cadastrado no sistema.","Atenção",JOptionPane.WARNING_MESSAGE);
             jLMotorista.setForeground(Color.red);
                 jLCPF.setForeground(Color.red);
                 jLCNH.setForeground(Color.red);
         }
                
           
            }
     }
     
     public void excluirMotorista() throws SQLException{
             UIManager.put("OptionPane.noButtonText", "Não");  
         UIManager.put("OptionPane.yesButtonText", "Sim");
         int resp = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este motorista?", "Confirmação", 0,3);
         if (resp == 0){
             atributos at = new atributos();
             at.setCodigoMotorista(Long.valueOf(jTCodigo.getText()));
               motoristaDao dao;
                dao = new motoristaDao();
                dao.excluirMotorista(at);
                mostraPesquisa(motoristas);
                JOptionPane.showMessageDialog(null,"Motorista excluído com sucesso.","Excluir",JOptionPane.INFORMATION_MESSAGE);
                listarMotoristas();
                limparDados();
               desabilitaDados();
         }
        }
     
     private void alteraMotorista() throws SQLException {
             String dataNasc="";
             String dataVenc="";
             String opVenc="";
                     
             if(jTPesquisa.getSelectedRow() != -1 || dados==1){
           
            UIManager.put("OptionPane.noButtonText", "Não");  
         UIManager.put("OptionPane.yesButtonText", "Sim");
            int resp = JOptionPane.showConfirmDialog(this, "Deseja realmente alterar este motorista?", "Confirmação", 0,3);
         if (resp == 0){
             if(verificaDados()){
                 
                 
                  String sql = "select *from motoristas";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery();
         boolean nome_igual=false;
         boolean cpf_igual=false;
         boolean cnh_igual=false;
        
         
          //Se o usuário tentar alterar o nome ou o cpf para algum já cadastrado o programa irá impedir
         if(!jTNome.getText().equals(nomeigual)){
             while(rs.next()){
             if(jTNome.getText().equals(rs.getString("nome"))){
                 nome_igual=true; 
             }
         }
         }
         
         if(!jTCPF.getText().equals(cpfigual)){
              while(rs.next()){
                  if(jTCPF.getText().equals(rs.getString("cpf"))){
                 cpf_igual=true;
             }
              }
         }
         
          if(!jTCNH.getText().equals(cnhigual)){
              while(rs.next()){
                  if(jTCNH.getText().equals(rs.getString("cnh"))){
                 cnh_igual=true;
             }
              }
         }
         
         rs.close();
         stmt.close();
        
         if(nome_igual==false && cpf_igual==false && cnh_igual==false){
             
              atributos at = new atributos();
             motoristaDao dao = new motoristaDao();
             
            at.setCodigoMotorista(Long.valueOf(jTCodigo.getText()));
            at.setNome(jTNome.getText());
            at.setDiaNasc(jCDiaNasc.getSelectedItem().toString());
            at.setMesNasc(jCMesNasc.getSelectedItem().toString());
            at.setAnoNasc(jCAnoNasc.getSelectedItem().toString());
                dataNasc = jCDiaNasc.getSelectedItem().toString();
      dataNasc = dataNasc+"/"+jCMesNasc.getSelectedItem().toString();
        dataNasc = dataNasc+"/"+jCAnoNasc.getSelectedItem();toString();
                at.setDtNasc(dataNasc);
            at.setRg(jTRG.getText());
            at.setCpf(jTCPF.getText());
            at.setCnh(jTCNH.getText());
            at.setCat(jCCat.getSelectedItem().toString());
           at.setDiaVenc(jCDia.getSelectedItem().toString());
            at.setMesVenc(jCMes.getSelectedItem().toString());
            at.setAnoVenc(jCAno.getSelectedItem().toString());
                dataVenc = jCDia.getSelectedItem().toString();
      dataVenc = dataVenc+"/"+jCMes.getSelectedItem().toString();
        dataVenc = dataVenc+"/"+jCAno.getSelectedItem();toString();
                at.setDtVenc(dataVenc);
                
                DateFormat year = new SimpleDateFormat("yyyy");//formato de data para ano
      Date date = new Date();
      String anoSistema = year.format(date);//Variável que recebe o ano atual do sistema
      
       int mes = date.getMonth()+1;//Variável que recebe o mês atual do sistema
                
       
       int mesvenc = Integer.valueOf(jCMes.getSelectedIndex())- mes;//Subtrai o mês de vencimento da carteira do motorista pelo mês atual do sistema
            int anovenc = Integer.valueOf(jCAno.getSelectedItem().toString())- Integer.valueOf(anoSistema);//Subtrai o ano de vencimento da carteira do motorista pelo ano atual do sistema
            
            if(anovenc<0 || anovenc==0 && mesvenc<=1){//Se a diferença entre o ano de vencimento e o ano do sistema for 0, indica que a carteira vence este ano, se a diferença entre o mes de vencimento e o mes atual do sistema for menor ou igual a 1, falta 1 mês para a cateira vencer ou já está vencida
                opVenc = "Sim";//Variável para jogar o valor pro campo opvenc no banco de dados
            }else{
                opVenc = "Não";
            }
                at.setOpVenc(opVenc);
             at.setCarreta(jTCarreta.getText());
             at.setAnoCarreta(jCAnoCar.getSelectedItem().toString());
             at.setModelo(jTModelo.getText());
             at.setPlaca(jTPlaca.getText());
             at.setAnoPlaca(jCAnoPlaca.getSelectedItem().toString());
             at.setCapTon(jTCap.getText());
             at.setTurno(jCTurno.getSelectedItem().toString());
             at.setPeriodo(jCPeriodo.getSelectedItem().toString());
            at.setTelRes(jFTelRes.getText());
                at.setTelCom(jFTelCom.getText());
                at.setCel(jFCel.getText());
                at.setId(jFId.getText());
                at.setTelRec(jFTelRec.getText());
                at.setEmail(jTEmail.getText());
                at.setRua(jTRua.getText());
                at.setNumero(jTNumero.getText());
                at.setCompl(jTComplemento.getText());
                at.setCep(jFTCep.getText());
                at.setBairro(jTBairro.getText());
                at.setCidade(jTCidade.getText());
                at.setUf(jCEstado.getSelectedItem().toString());
                at.setPais(jTPais.getText());
           
            dao.alteraMotorista(at);
                JOptionPane.showMessageDialog(null,"Motorista alterado com sucesso.","Alterar",JOptionPane.INFORMATION_MESSAGE);
                  listarMotoristas();
                 desabilitaDados();
                 
         }else{
                 JOptionPane.showMessageDialog(this, "Este nome, CPF ou CNH já está cadastrado no sistema.","Atenção",JOptionPane.WARNING_MESSAGE);
           jLMotorista.setForeground(Color.red);
                 jLCPF.setForeground(Color.red);
                 jLCNH.setForeground(Color.red);
             }
              
            }
        }
         }
            
         }
     
     
     
      //Realiza a pesquisa de acordo com o tipo selecionado
      protected void listarMotoristas() throws SQLException{
        motoristaDao dao = new motoristaDao ();
        
        switch(jCPesquisa.getSelectedIndex()){
            case 0:
                 motoristas = dao.getListaNomeMotorista("%" + jTPesquisar.getText() + "%");
        mostraPesquisa(motoristas);
                break;
              
            case 1:
                motoristas = dao.getListaTurno("%" + jTPesquisar.getText() + "%");
        mostraPesquisa(motoristas);
                break;
      
            case 2:
                motoristas = dao.getListaPlaca("%" + jTPesquisar.getText() + "%");
        mostraPesquisa(motoristas);
                break;  
                
            case 3:
                 motoristas = dao.getListaModelo("%" + jTPesquisar.getText() + "%");
        mostraPesquisa(motoristas);
                break; 
                
            case 4:
                 motoristas = dao.getListaCnh("%" + jTPesquisar.getText() + "%");
        mostraPesquisa(motoristas);
                break;
                
            case 5:
                  motoristas = dao.getListaDt("%" + jTPesquisar.getText() + "%");
        mostraPesquisa(motoristas);
                break;
           
        }      
    }
  
      //Mostra os dados pesquisados na tabela
   private void mostraPesquisa(List<atributos> contas) {
      while(tmMotoristas.getRowCount()>0){
          tmMotoristas.removeRow(0);
      }
        if(motoristas.isEmpty()){
            
         
      }else{
          String []linha = new String [] {null, null, null, null, null,null,null};
          for (int i = 0; i < motoristas.size(); i++){
              tmMotoristas.addRow(linha);
              tmMotoristas.setValueAt(contas.get(i).getNome(), i, 0);
              tmMotoristas.setValueAt(contas.get(i).getTurno(), i, 1);
              tmMotoristas.setValueAt(contas.get(i).getPlaca(), i, 2);
              tmMotoristas.setValueAt(contas.get(i).getCarreta(), i, 3);
              tmMotoristas.setValueAt(contas.get(i).getModelo(), i, 4);
              tmMotoristas.setValueAt(contas.get(i).getCnh(), i, 5);
              tmMotoristas.setValueAt(contas.get(i).getDtVenc(), i, 6);
          }
      }
    }
   int dados =0;
   
    //Os TextField recebem os dados selecionados no form Inicio
     public void recebeDados(atributos at){
       
          habilitaDados();
            jBSalvar.setEnabled(false);
            jBAlterar.setEnabled(true);
            jBImprimir.setEnabled(true);
            jBExcluir.setEnabled(true);
             jTCodigo.setText(String.valueOf(at.getCodigoMotorista()));
             jTNome.setText(at.getNome());
        jCDiaNasc.setSelectedItem(at.getDiaNasc());
        jCMesNasc.setSelectedItem(at.getMesNasc());
        jCAnoNasc.setSelectedItem(at.getAnoNasc());
           jTCPF.setText(at.getCpf());
           jTRG.setText(at.getRg());
           jTCNH.setText(at.getCnh());
           jCCat.setSelectedItem(at.getCat());
        jCDia.setSelectedItem(at.getDiaVenc());
        jCMes.setSelectedItem(at.getMesVenc());
        jCAno.setSelectedItem(at.getAnoVenc());
        jTCarreta.setText(at.getCarreta());
        jCAnoCar.setSelectedItem(at.getAnoCarreta());
         jTModelo.setText(at.getModelo());
         jTPlaca.setText(at.getPlaca());
        jCAnoPlaca.setSelectedItem(at.getPlaca());
         jTCap.setText(at.getCapTon());
        jCTurno.setSelectedItem(at.getTurno());
        jCPeriodo.setSelectedItem(at.getPeriodo());
        jFTelRes.setText(at.getTelRes());
        jFTelCom.setText(at.getTelCom());
        jFCel.setText(at.getCel());
        jFId.setText(at.getId());
        jFTelRec.setText(at.getTelRec());
        jTEmail.setText(at.getEmail());
        jFTCep.setText(at.getCep());
        jTRua.setText(at.getRua());
        jTNumero.setText(at.getNumero());
        jTComplemento.setText(at.getCompl());
        jTBairro.setText(at.getBairro());
        jTCidade.setText(at.getCidade());
        jCEstado.setSelectedItem(at.getUf());
        jTPais.setText(at.getPais());
         dados =1;
         nomeigual = jTNome.getText();
        cpfigual = jTCPF.getText();
        cnhigual = jTCNH.getText();
        
         }
   
     
     //Joga os dados selecionados nas TexfFields
  private void linhaSelecionada(JTable tabela){
          
        if(jTPesquisa.getSelectedRow()!=-1){
            habilitaDados();
            jBSalvar.setEnabled(false);
            jBAlterar.setEnabled(true);
            jBImprimir.setEnabled(true);
            jBExcluir.setEnabled(true);
        jTCodigo.setText(String.valueOf(motoristas.get(tabela.getSelectedRow()).getCodigoMotorista()));
        jTNome.setText(motoristas.get(tabela.getSelectedRow()).getNome());
        jCDiaNasc.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getDiaNasc());
        jCMesNasc.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getMesNasc());
        jCAnoNasc.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getAnoNasc());
        jTRG.setText(motoristas.get(tabela.getSelectedRow()).getRg());
        jTCPF.setText(motoristas.get(tabela.getSelectedRow()).getCpf());
        jTCNH.setText(motoristas.get(tabela.getSelectedRow()).getCnh());
        jCCat.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getCat());
        jCDia.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getDiaVenc());
        jCMes.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getMesVenc());
        jCAno.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getAnoVenc());
        jTCarreta.setText(motoristas.get(tabela.getSelectedRow()).getCarreta());
        jCAnoCar.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getAnoCarreta());
        jTModelo.setText(motoristas.get(tabela.getSelectedRow()).getModelo());
        jTPlaca.setText(motoristas.get(tabela.getSelectedRow()).getPlaca());
        jCAnoPlaca.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getAnoPlaca());
        jTCap.setText(motoristas.get(tabela.getSelectedRow()).getCapTon());
        jCTurno.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getTurno());
        jCPeriodo.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getPeriodo());
        jFTelRes.setText(motoristas.get(tabela.getSelectedRow()).getTelRes());
        jFTelCom.setText(motoristas.get(tabela.getSelectedRow()).getTelCom());
        jFCel.setText(motoristas.get(tabela.getSelectedRow()).getCel());
        jFId.setText(motoristas.get(tabela.getSelectedRow()).getId());
        jFTelRec.setText(motoristas.get(tabela.getSelectedRow()).getTelRec());
        jTEmail.setText(motoristas.get(tabela.getSelectedRow()).getEmail());
        jTRua.setText(motoristas.get(tabela.getSelectedRow()).getRua());
        jTNumero.setText(motoristas.get(tabela.getSelectedRow()).getNumero());
        jTComplemento.setText(motoristas.get(tabela.getSelectedRow()).getCompl());
        jFTCep.setText(motoristas.get(tabela.getSelectedRow()).getCep());
        jTBairro.setText(motoristas.get(tabela.getSelectedRow()).getBairro());
        jTCidade.setText(motoristas.get(tabela.getSelectedRow()).getCidade());
        jCEstado.setSelectedItem(motoristas.get(tabela.getSelectedRow()).getUf());
        jTPais.setText(motoristas.get(tabela.getSelectedRow()).getPais());
        
              jLMotorista.setForeground(Color.BLACK);
                jLDtNasc.setForeground(Color.black);
                jLRG.setForeground(Color.black);
                jLCPF.setForeground(Color.black);
                jLCNH.setForeground(Color.black);
               jLVencimento.setForeground(Color.black);
                jLCarreta.setForeground(Color.black);
               jLModelo.setForeground(Color.black);
                jLPlaca.setForeground(Color.black);
               jLCap.setForeground(Color.black);
                 jLCep.setForeground(Color.black);
                 jLRua.setForeground(Color.black);
                 jLNumero.setForeground(Color.black);
                jLBairro.setForeground(Color.BLACK);
                jLCidade.setForeground(Color.BLACK);
                jLPais.setForeground(Color.BLACK);
        
        nomeigual = jTNome.getText();
        cpfigual = jTCPF.getText();
        cnhigual = jTCNH.getText();
        
         nome = jTNome.getText();
        rg = jTRG.getText();
        cpf = jTCPF.getText();
        cnh = jTCNH.getText();
        carreta = jTCarreta.getText();
        modelo = jTModelo.getText();
        placa = jTPlaca.getText();
        capton = jTCap.getText();
        tel = jFTelRes.getText();
        telCom = jFTelCom.getText();
        cel = jFCel.getText();
        id = jFId.getText();
        telRec = jFTelRec.getText();
        email = jTEmail.getText();
        cep = jFTCep.getText();
        rua = jTRua.getText();
        numero = jTNumero.getText();
        compl = jTComplemento.getText();
        bairro = jTBairro.getText();
        cidade = jTCidade.getText();
        pais = jTPais.getText();
        jTCadastro.setSelectedComponent(jPCadastro);
        validaDataNasc();
        validaDataVenc();
              jLValidacao.setVisible(false);
   
        } else{
           limparDados();
       }
       
      
       jTPesquisar.setText("");
    }
  
  //Passa todo o nome digitado para letras maiusculas
         public void upperCase(){
          jTNome.setText(jTNome.getText().toUpperCase());
          jTCarreta.setText(jTCarreta.getText().toUpperCase());
          jTModelo.setText(jTModelo.getText().toUpperCase());
          jTPlaca.setText(jTPlaca.getText().toUpperCase());
         }
        
         //Método para determinar se o cpf é válido ou não
         public void validaCPF(){
             if(jTCPF.getText().length()==11){
            String CPF = jTCPF.getText();
        int n1 = CPF.charAt(0) - 48;        int n2 = CPF.charAt(1) - 48;
        int n3 = CPF.charAt(2) - 48;        int n4 = CPF.charAt(3) - 48;
        int n5 = CPF.charAt(4) - 48;        int n6 = CPF.charAt(5) - 48;
        int n7 = CPF.charAt(6) - 48;        int n8 = CPF.charAt(7) - 48;
        int n9 = CPF.charAt(8) - 48;        int n10 = CPF.charAt(9) - 48;
        int n11 = CPF.charAt(10) - 48;
        //Calculo aqui
        int sm = (n1*10)+(n2*9)+(n3*8)+(n4*7)+(n5*6)+(n6*5)+(n7*4)+(n8*3)+(n9*2);
        int DV1=11 - (sm % 11);
        int sm2= (n1*11)+(n2*10)+(n3*9)+(n4*8)+(n5*7)+(n6*6)+(n7*5)+(n8*4)+(n9*3)+(n10*2);
        int DV2=11 - (sm2 % 11);
        if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222")
            || CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555")
            || CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888")
            || CPF.equals("99999999999"))
        {
            jLValidacao.setVisible(true);
            jLValidacao.setForeground(Color.RED);
            jLValidacao.setText("CPF Inválido");
                
        }else{
             if((DV1==n10||(DV1==10&&n10==0)||(DV1==11&&n10==0))&&(DV2==n11||(DV2==10&&n11==0)||(DV2==11&&n11==0))){
               jLValidacao.setVisible(true);
            jLValidacao.setForeground(Color.BLACK);
            jLValidacao.setText("CPF Válido");
            //System.out.println(DV1+" "+DV2);
                
            }else{
             jLValidacao.setVisible(true);
            jLValidacao.setForeground(Color.RED);
            jLValidacao.setText("CPF Inválido");
            }
        }
        }
         }
  
   //Verifica se a data é inválida. Exemplo: 30/02/2012
         public void validaDataNasc(){

    if(jCDiaNasc.getSelectedIndex()!=0 && jCMesNasc.getSelectedIndex()!=0 && jCAnoNasc.getSelectedIndex()!=0 ){
        int ano = Integer.parseInt(jCAnoNasc.getSelectedItem().toString()); 
          if(jCMesNasc.getSelectedIndex()==2 && jCDiaNasc.getSelectedIndex()>29 ||(jCMesNasc.getSelectedIndex()==4 && jCDiaNasc.getSelectedIndex()>30 ||jCMesNasc.getSelectedIndex()==6 && jCDiaNasc.getSelectedIndex()>30
              ||jCMesNasc.getSelectedIndex()==9 && jCDiaNasc.getSelectedIndex()>30 ||jCMesNasc.getSelectedIndex()==11 && jCDiaNasc.getSelectedIndex()>30 
               ||jCDiaNasc.getSelectedIndex()==29 && jCMesNasc.getSelectedIndex()==2 && ano%4!=0)){
           jLDtInvalida.setVisible(true);
           jLDtInvalida.setText("Data inválida");
           jLDtInvalida.setForeground(Color.RED);
       }else{
            jLDtInvalida.setText("");  
            jLDtInvalida.setVisible(false);
       }
    }else{
        jLDtInvalida.setText("");  
            jLDtInvalida.setVisible(false);
    }
  
}
         
          public void sair(){
             UIManager.put("OptionPane.noButtonText", "Não");  
         UIManager.put("OptionPane.yesButtonText", "Sim");
         int x = JOptionPane.showConfirmDialog(this, "Deseja encerrar o cadastro de motoristas?", "Alerta",0, 2);
        
        if (x==0) {
       this.dispose();
        }
    }
         
         public void validaDataVenc(){

    if(jCDia.getSelectedIndex()!=0 && jCMes.getSelectedIndex()!=0 && jCAno.getSelectedIndex()!=0 ){
        int ano = Integer.parseInt(jCAno.getSelectedItem().toString()); 
          if(jCMes.getSelectedIndex()==2 && jCDia.getSelectedIndex()>29 ||(jCMes.getSelectedIndex()==4 && jCDia.getSelectedIndex()>30 ||jCMes.getSelectedIndex()==6 && jCDia.getSelectedIndex()>30
              ||jCMes.getSelectedIndex()==9 && jCDia.getSelectedIndex()>30 ||jCMes.getSelectedIndex()==11 && jCDia.getSelectedIndex()>30 
               ||jCDia.getSelectedIndex()==29 && jCMes.getSelectedIndex()==2 && ano%4!=0)){
           jLDtInvalida1.setVisible(true);
           jLDtInvalida1.setText("Data inválida");
           jLDtInvalida1.setForeground(Color.RED);
       }else{
            jLDtInvalida1.setText("");  
            jLDtInvalida1.setVisible(false);
       }
    }else{
        jLDtInvalida1.setText("");  
            jLDtInvalida1.setVisible(false);
    }
  
}
         
          //Método para preencher o endereço automaticamente de acordo com o cep
         public void preencheEnd(){
             String uf="";
             switch(jCEstado.getSelectedIndex()){
                 case 0:
                    uf = "ac";
                     break;
                 case 1:
                     uf = "al";
                     break;
                 case 2:
                     uf = "am";
                     break;
                 case 3:
                     uf = "ap";
                     break;
                 case 4:
                     uf = "ba";
                     break;
                 case 5:
                     uf = "ce";
                     break;
                 case 6:
                     uf = "df";
                     break;
                 case 7:
                     uf = "es";
                     break;
                 case 8:
                     uf = "go";
                     break;
                 case 9:
                     uf = "ma";
                     break;
                 case 10:
                     uf = "mg";
                     break;
                 case 11:
                     uf = "ms";
                     break;
                 case 12:
                     uf = "mt";
                     break;
                 case 13:
                     uf = "pa";
                     break;
                 case 14:
                     uf = "pb";
                     break;
                 case 15:
                     uf = "pe";
                     break;
                 case 16:
                     uf = "pi";
                     break;
                 case 17:
                     uf = "pr";
                     break;
                 case 18:
                     uf = "rj";
                     break;
                 case 19:
                     uf = "rn";
                     break;
                 case 20:
                     uf = "ro";
                     break;
                 case 21:
                     uf = "rr";
                     break;
                 case 22:
                     uf = "rs";
                     break;
                 case 23:
                     uf = "sc";
                     break;
                 case 24:
                     uf = "se";
                     break;
                 case 25:
                     uf = "sp";
                     break;
                 case 26:
                     uf = "toc";
                     break;
                     
             }
             
               try{
             String sql = "select *from "+uf+" where cep like ?";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, jFTCep.getText());
         ResultSet rs = stmt.executeQuery();
         
         while(rs.next()){
             jTRua.setText(rs.getString("tp_logradouro")+" "+rs.getString("logradouro"));
             jTBairro.setText(rs.getString("bairro"));
             jTCidade.setText(rs.getString("cidade"));
             jTPais.setText("Brasil");
         }
         
         rs.close();
         stmt.close();
         
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Erro ao pesquisar cep.\nErro: "+ex);
        }
         }
    
       public void limparDados(){
            jTCodigo.setText("");
            jTNome.setText("");
            jCDiaNasc.setSelectedIndex(0);
            jCMesNasc.setSelectedIndex(0);
            jCAnoNasc.setSelectedIndex(0);
            jTRG.setText("");
            jTCPF.setText("");
            jTCNH.setText("");
            jCCat.setSelectedIndex(0);
            jCDia.setSelectedIndex(0);
            jCMes.setSelectedIndex(0);
            jCAno.setSelectedIndex(0);
            jTCarreta.setText("");
            jCAnoCar.setSelectedIndex(0);
            jTModelo.setText("");
            jTPlaca.setText("");
            jCAnoPlaca.setSelectedIndex(0);
            jTCap.setText("");
            jCTurno.setSelectedIndex(0);
            jCPeriodo.setSelectedIndex(0);
            jLValidacao.setVisible(false);
            jFTelRes.setText("");
            jFTelCom.setText("");
            jFCel.setText("");
            jFId.setText("");
            jFTelRec.setText("");
            jTEmail.setText("");
            jTRua.setText("");
            jTNumero.setText("");
            jTComplemento.setText("");
            jFTCep.setText("");
            jTBairro.setText("");
            jTCidade.setText("");
            jCEstado.setSelectedIndex(0);
            jTPais.setText("");
            nome = "";
            rg = "";
            cpf="";
            cnh="";
            carreta="";
            modelo="";
            placa="";
            capton="";
       tel ="";
       telCom="";
       cel ="";
       id = "";
       telRec="";
        rua="";
       numero="";
       bairro="";
       cidade="";
       compl="";
       cidade="";
       pais="";
       cep = "";
        jLMotorista.setForeground(Color.BLACK);
                jLDtNasc.setForeground(Color.BLACK);
                jLRG.setForeground(Color.BLACK);
                jLCPF.setForeground(Color.BLACK);
                jLCNH.setForeground(Color.BLACK);
                jLCat.setForeground(Color.BLACK);
                jLVencimento.setForeground(Color.BLACK);
                jLCep.setForeground(Color.BLACK);
                jLRua.setForeground(Color.BLACK);
                jLNumero.setForeground(Color.BLACK);
                jLBairro.setForeground(Color.BLACK);
                jLCidade.setForeground(Color.BLACK);
                jLPais.setForeground(Color.BLACK);
               
        }
       
       public boolean verificaDados(){
           String msgerro="";
        if(jTNome.getText().equals("") || jCDiaNasc.getSelectedIndex()==0 || jCMesNasc.getSelectedIndex()==0 || jCAnoNasc.getSelectedIndex()==0 || 
                 jLDtInvalida.getText().equals("Data inválida") || jTRG.getText().equals("") || jTCPF.getText().length()!=11 || 
                jTCPF.getText().length()==11 && jLValidacao.getText().equals("CPF Inválido") || jTCNH.getText().equals("")|| 
                jCDia.getSelectedIndex()==0 || jCMes.getSelectedIndex()==0 || jCAno.getSelectedIndex()==0 || 
                 jLDtInvalida1.getText().equals("Data inválida") || jTCarreta.getText().equals("") || jTModelo.getText().equals("") || jTPlaca.getText().equals("") || jTCap.getText().equals("") 
                || jFTelRes.getText().length()!=14 
                && jFTelCom.getText().length()!=14  && jFCel.getText().length()<14  && jFId.getText().equals("") && jFTelRec.getText().length()!=14
                && jTEmail.getText().equals("")|| jTRua.getText().equals("") || jTNumero.getText().equals("") || jFTCep.getText().length()!=9 ||
                jTBairro.getText().equals("") || jTCidade.getText().equals("") || jTPais.getText().equals("")){
           
             
            if(jTPais.getText().equals("")){
                    jLPais.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPEnd);
                    jTPais.requestFocus();
                }else{
             jLPais.setForeground(Color.black);
        }
                
                if(jTCidade.getText().equals("")){
                    jLCidade.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPEnd);
                    jTCidade.requestFocus();
                }else{
                    jLCidade.setForeground(Color.black);
                }
                
                if(jTBairro.getText().equals("")){
                    jLBairro.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPEnd);
                    jTBairro.requestFocus();
                }else{
                     jLBairro.setForeground(Color.black);
                }
                
                if(jTNumero.getText().equals("")){
                    jLNumero.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPEnd);
                    jTNumero.requestFocus();
                }else{
                    jLNumero.setForeground(Color.black);
                }
                
                if(jTRua.getText().equals("")){
                    jLRua.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPEnd);
                    jTRua.requestFocus();
                }else{
                    jLRua.setForeground(Color.black);
                }
                
                if(jFTCep.getText().length()!=9){
                    jLCep.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPEnd);
                    jFTCep.requestFocus();
                }else{
                     jLCep.setForeground(Color.black);
                }
                
                if(jFTelRes.getText().length()!=14 && jFTelCom.getText().length()!=14 &&
                        jFCel.getText().length()<14 && jFId.getText().equals("")&&
                        jFTelRec.getText().length()!=14 && jTEmail.getText().equals("")){
                    msgerro = "Preencha pelo menos um campo de contato";
                    jTCadastro.setSelectedComponent(jPEnd);
                    jFTelRes.requestFocus();
                }
                
                if(jTCap.getText().equals("")){
                    jLCap.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPCadastro);
                    jTCap.requestFocus();
                }else{
                    jLCap.setForeground(Color.black);
                }
                
                 if(jTPlaca.getText().equals("")){
                    jLPlaca.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPCadastro);
                    jTPlaca.requestFocus();
                }else{
                    jLPlaca.setForeground(Color.black);
                }
                 
                  if(jTModelo.getText().equals("")){
                    jLModelo.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPCadastro);
                    jTModelo.requestFocus();
                }else{
                    jLModelo.setForeground(Color.black);
                }
                  
                  if(jTCarreta.getText().equals("")){
                    jLCarreta.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPCadastro);
                    jTCarreta.requestFocus();
                }else{
                    jLCarreta.setForeground(Color.black);
                }
                  
                  if(jCDia.getSelectedIndex()==0 || jCMes.getSelectedIndex()==0 || jCAno.getSelectedIndex()==0
                        || jLDtInvalida1.getText().equals("Data inválida")){
                    jLVencimento.setForeground(Color.red);
                   jTCadastro.setSelectedComponent(jPCadastro);
                    jCDia.requestFocus();
                }else{
                     jLVencimento.setForeground(Color.black);
                }
                
                  if(jTCNH.getText().equals("")){
                    jLCNH.setForeground(Color.red);
                   jTCadastro.setSelectedComponent(jPCadastro);
                    jTCNH.requestFocus();
                }else{
                    jLCNH.setForeground(Color.black);
                }
                  
                if(jTCPF.getText().length()!=11 ||  jTCPF.getText().length()==11 && jLValidacao.getText().equals("CPF Inválido") ){
                    jLCPF.setForeground(Color.red);
                   jTCadastro.setSelectedComponent(jPCadastro);
                    jTCPF.requestFocus();
                }else{
                      jLCPF.setForeground(Color.black);
                }
                
                if(jTRG.getText().equals("")){
                    jLRG.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPCadastro);
                    jTRG.requestFocus();
                }else{
                    jLRG.setForeground(Color.black);
                }
                
                 if(jCDiaNasc.getSelectedIndex()==0 || jCMesNasc.getSelectedIndex()==0 || jCAnoNasc.getSelectedIndex()==0
                        || jLDtInvalida.getText().equals("Data inválida")){
                    jLDtNasc.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPCadastro);
                    jCDiaNasc.requestFocus();
                }else{
                     jLDtNasc.setForeground(Color.black);
                }
                
                if(jTNome.getText().equals("")){
                    jLMotorista.setForeground(Color.red);
                    jTCadastro.setSelectedComponent(jPCadastro);
                    jTNome.requestFocus();
                }else{
                    jLMotorista.setForeground(Color.black);
                }
                
                 JOptionPane.showMessageDialog(null,"Há campos obrigatórios não preenchidos.\n"+msgerro,"Atenção", JOptionPane.WARNING_MESSAGE);
                
        return false;
        }else{
             jLMotorista.setForeground(Color.BLACK);
                jLDtNasc.setForeground(Color.black);
                jLRG.setForeground(Color.black);
                jLCPF.setForeground(Color.black);
                jLCNH.setForeground(Color.black);
               jLVencimento.setForeground(Color.black);
                jLCarreta.setForeground(Color.black);
               jLModelo.setForeground(Color.black);
                jLPlaca.setForeground(Color.black);
               jLCap.setForeground(Color.black);
                 jLCep.setForeground(Color.black);
                 jLRua.setForeground(Color.black);
                 jLNumero.setForeground(Color.black);
                jLBairro.setForeground(Color.BLACK);
                jLCidade.setForeground(Color.BLACK);
                jLPais.setForeground(Color.BLACK);
            return true;
        }
        
    }
       
       //Desabilita os campos para edição
         public void desabilitaDados(){
            jTNome.setEditable(false); 
            jCDiaNasc.setEnabled(false);
            jCMesNasc.setEnabled(false);
            jCAnoNasc.setEnabled(false);
            jTCPF.setEditable(false);
            jTRG.setEditable(false);
            jTCNH.setEditable(false);
            jCCat.setEnabled(false);
            jCDia.setEnabled(false);
            jCMes.setEnabled(false);
            jCAno.setEnabled(false);
            jTCarreta.setEditable(false);
            jCAnoCar.setEnabled(false);
            jTModelo.setEditable(false);
            jTPlaca.setEditable(false);
            jCAnoPlaca.setEnabled(false);
            jTCap.setEditable(false);
            jCTurno.setEnabled(false);
            jCPeriodo.setEnabled(false);
            jFTelRes.setEditable(false);
            jFTelCom.setEditable(false);
            jFCel.setEditable(false);
            jFId.setEditable(false);
            jFTelRec.setEditable(false);
            jTEmail.setEditable(false);
            jTRua.setEditable(false);
            jTNumero.setEditable(false);
            jTComplemento.setEditable(false);
            jFTCep.setEditable(false);
            jTBairro.setEditable(false);
            jTCidade.setEditable(false);
            jCEstado.setEnabled(false);
            jTPais.setEditable(false); 
            jBSalvar.setEnabled(false);
            jBExcluir.setEnabled(false);
            jBCancelar.setEnabled(false);
           jBImprimir.setEnabled(false);
           jBAlterar.setEnabled(false);
           jBNovo.setEnabled(true);
        }
         
         //Habilita os campos para edição
         public void habilitaDados(){
            jTNome.setEditable(true); 
            jCDiaNasc.setEnabled(true);
            jCMesNasc.setEnabled(true);
            jCAnoNasc.setEnabled(true);
            jTCPF.setEditable(true);
            jTRG.setEditable(true);
            jTCNH.setEditable(true);
            jCCat.setEnabled(true);
            jCDia.setEnabled(true);
            jCMes.setEnabled(true);
            jCAno.setEnabled(true);
            jTCarreta.setEditable(true);
            jCAnoCar.setEnabled(true);
            jTModelo.setEditable(true);
            jTPlaca.setEditable(true);
            jCAnoPlaca.setEnabled(true);
            jTCap.setEditable(true);
            jCTurno.setEnabled(true);
            jCPeriodo.setEnabled(true);
            jFTelRes.setEditable(true);
            jFTelCom.setEditable(true);
            jFCel.setEditable(true);
            jFId.setEditable(true);
            jFTelRec.setEditable(true);
            jTEmail.setEditable(true);
            jTRua.setEditable(true);
            jTNumero.setEditable(true);
            jTComplemento.setEditable(true);
            jFTCep.setEditable(true);
            jTBairro.setEditable(true);
            jTCidade.setEditable(true);
            jCEstado.setEnabled(true);
            jTPais.setEditable(true); 
            jBSalvar.setEnabled(true);
           jBCancelar.setEnabled(true);
           jBNovo.setEnabled(false);
        }
         
         public void impressaoMotorista() {
	 
	    // note que estamos chamando o novo relatório
	    InputStream inputStream = getClass().getResourceAsStream( "/Motoristas.jasper" );
	 
	    // mapa de parâmetros do relatório
	    Map parametros = new HashMap();
	 
	    /*
	     * Insere o parâmetro primeiroNome no mapa, com o valor F%
	     * ou seja, todos os clientes que tenham primeiro nome começando
	     * com a letra F.
	     */
	    parametros.put( "codigoMotorista", jTCodigo.getText() );
	 
	    // outros possíveis parâmetros aqui...
	 
	    try {
	 
	        // abre o relatório
	        Impressao.openReport( "Motoristas", inputStream, parametros,
	                ConexaoImpressao.getConnection() );
                
	  this.dispose();
	    } catch ( SQLException exc ) {
	        exc.printStackTrace();
	    } catch ( JRException exc ) {
	        exc.printStackTrace();
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

        jTCadastro = new javax.swing.JTabbedPane();
        jPCadastro = new javax.swing.JPanel();
        jPDados = new javax.swing.JPanel();
        jLCod = new javax.swing.JLabel();
        jLMotorista = new javax.swing.JLabel();
        jLRG = new javax.swing.JLabel();
        jLCPF = new javax.swing.JLabel();
        jLCNH = new javax.swing.JLabel();
        jLCarreta = new javax.swing.JLabel();
        jLModelo = new javax.swing.JLabel();
        jLPlaca = new javax.swing.JLabel();
        jLCap = new javax.swing.JLabel();
        jLVencimento = new javax.swing.JLabel();
        jTCodigo = new javax.swing.JTextField();
        jTNome = new javax.swing.JTextField();
        jTRG = new javax.swing.JTextField();
        jTCPF = new javax.swing.JTextField();
        jTCNH = new javax.swing.JTextField();
        jTCarreta = new javax.swing.JTextField();
        jTModelo = new javax.swing.JTextField();
        jTPlaca = new javax.swing.JTextField();
        jTCap = new javax.swing.JTextField();
        jTTurno = new javax.swing.JLabel();
        jCTurno = new javax.swing.JComboBox();
        jLDtNasc = new javax.swing.JLabel();
        jCDiaNasc = new javax.swing.JComboBox();
        jLSeparador1 = new javax.swing.JLabel();
        jCMesNasc = new javax.swing.JComboBox();
        jLSeparador2 = new javax.swing.JLabel();
        jCAnoNasc = new javax.swing.JComboBox();
        jCAno = new javax.swing.JComboBox();
        jLSeparador4 = new javax.swing.JLabel();
        jCMes = new javax.swing.JComboBox();
        jLSeparador3 = new javax.swing.JLabel();
        jCDia = new javax.swing.JComboBox();
        jLCat = new javax.swing.JLabel();
        jCCat = new javax.swing.JComboBox();
        jLAnoCar = new javax.swing.JLabel();
        jLAnoPl = new javax.swing.JLabel();
        jCPeriodo = new javax.swing.JComboBox();
        jLPeriodo = new javax.swing.JLabel();
        jLDtInvalida = new javax.swing.JLabel();
        jLValidacao = new javax.swing.JLabel();
        jLDtInvalida1 = new javax.swing.JLabel();
        jCAnoCar = new javax.swing.JComboBox();
        jCAnoPlaca = new javax.swing.JComboBox();
        jPEnd = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPContato = new javax.swing.JPanel();
        jLTelRes = new javax.swing.JLabel();
        jLTelCom = new javax.swing.JLabel();
        jLCel = new javax.swing.JLabel();
        jLId = new javax.swing.JLabel();
        jLTelRec = new javax.swing.JLabel();
        jLEmail = new javax.swing.JLabel();
        jFTelRes = new javax.swing.JFormattedTextField();
        jFTelCom = new javax.swing.JFormattedTextField();
        jFId = new javax.swing.JFormattedTextField();
        jFTelRec = new javax.swing.JFormattedTextField();
        jTEmail = new javax.swing.JTextField();
        jFCel = new javax.swing.JFormattedTextField();
        jPEndereco = new javax.swing.JPanel();
        jLRua = new javax.swing.JLabel();
        jLNumero = new javax.swing.JLabel();
        jLComplemento = new javax.swing.JLabel();
        jLBairro = new javax.swing.JLabel();
        jLUf = new javax.swing.JLabel();
        jTRua = new javax.swing.JTextField();
        jTNumero = new javax.swing.JTextField();
        jTComplemento = new javax.swing.JTextField();
        jTBairro = new javax.swing.JTextField();
        jLCidade = new javax.swing.JLabel();
        jTCidade = new javax.swing.JTextField();
        jCEstado = new javax.swing.JComboBox();
        jLPais = new javax.swing.JLabel();
        jTPais = new javax.swing.JTextField();
        jLCep = new javax.swing.JLabel();
        jFTCep = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        JPPesquisa = new javax.swing.JPanel();
        JPPesquisar = new javax.swing.JPanel();
        jPBusca = new javax.swing.JPanel();
        jLPesquisa = new javax.swing.JLabel();
        jCPesquisa = new javax.swing.JComboBox();
        jTPesquisar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTPesquisa = new javax.swing.JTable();
        jPBotoes = new javax.swing.JPanel();
        jBAlterar = new javax.swing.JButton();
        jBExcluir = new javax.swing.JButton();
        jBImprimir = new javax.swing.JButton();
        jBNovo = new javax.swing.JButton();
        jBSalvar = new javax.swing.JButton();
        jBPesquisar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jLBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CaminhoCerto");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        jPCadastro.setPreferredSize(new java.awt.Dimension(451, 448));

        jPDados.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPDados.setForeground(new java.awt.Color(0, 51, 102));
        jPDados.setPreferredSize(new java.awt.Dimension(451, 448));

        jLCod.setText("Código:");

        jLMotorista.setText("Nome:");

        jLRG.setText("RG:");

        jLCPF.setText("CPF:");

        jLCNH.setText("CNH.:");

        jLCarreta.setText("Carreta:");

        jLModelo.setText("Modelo:");

        jLPlaca.setText("            Placa (Cavalo):");

        jLCap.setText("Cap/ton.:");

        jLVencimento.setText("Vencimento:");

        jTCodigo.setEditable(false);

        jTNome.setEditable(false);
        jTNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTNomeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTNomeKeyReleased(evt);
            }
        });

        jTRG.setEditable(false);
        jTRG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTRGKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTRGKeyReleased(evt);
            }
        });

        jTCPF.setEditable(false);
        jTCPF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTCPFKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTCPFKeyReleased(evt);
            }
        });

        jTCNH.setEditable(false);
        jTCNH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTCNHKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTCNHKeyReleased(evt);
            }
        });

        jTCarreta.setEditable(false);
        jTCarreta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTCarretaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTCarretaKeyReleased(evt);
            }
        });

        jTModelo.setEditable(false);
        jTModelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTModeloKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTModeloKeyReleased(evt);
            }
        });

        jTPlaca.setEditable(false);
        jTPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTPlacaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTPlacaKeyReleased(evt);
            }
        });

        jTCap.setEditable(false);
        jTCap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTCapKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTCapKeyReleased(evt);
            }
        });

        jTTurno.setText("Turno:");

        jCTurno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Turno 1", "Turno 2", "Ambos" }));
        jCTurno.setEnabled(false);
        jCTurno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCTurnoKeyPressed(evt);
            }
        });

        jLDtNasc.setText("Data  de Nascimento:");

        jCDiaNasc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dia", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        jCDiaNasc.setEnabled(false);
        jCDiaNasc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCDiaNascActionPerformed(evt);
            }
        });
        jCDiaNasc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCDiaNascKeyPressed(evt);
            }
        });

        jLSeparador1.setText("/");

        jCMesNasc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mês", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" }));
        jCMesNasc.setEnabled(false);
        jCMesNasc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCMesNascActionPerformed(evt);
            }
        });
        jCMesNasc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCMesNascKeyPressed(evt);
            }
        });

        jLSeparador2.setText("/");

        jCAnoNasc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ano" }));
        jCAnoNasc.setEnabled(false);
        jCAnoNasc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCAnoNascActionPerformed(evt);
            }
        });
        jCAnoNasc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCAnoNascKeyPressed(evt);
            }
        });

        jCAno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ano" }));
        jCAno.setEnabled(false);
        jCAno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCAnoActionPerformed(evt);
            }
        });
        jCAno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCAnoKeyPressed(evt);
            }
        });

        jLSeparador4.setText("/");

        jCMes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mês", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" }));
        jCMes.setEnabled(false);
        jCMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCMesActionPerformed(evt);
            }
        });
        jCMes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCMesKeyPressed(evt);
            }
        });

        jLSeparador3.setText("/");

        jCDia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dia", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        jCDia.setEnabled(false);
        jCDia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCDiaActionPerformed(evt);
            }
        });
        jCDia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCDiaKeyPressed(evt);
            }
        });

        jLCat.setText("Categoria:");

        jCCat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "B", "C", "D", "E", "AB", "AC", "AD", "AE" }));
        jCCat.setEnabled(false);
        jCCat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCCatKeyPressed(evt);
            }
        });

        jLAnoCar.setText("Ano (Carreta):");

        jLAnoPl.setText("Ano (Placa):");

        jCPeriodo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Manhã", "Tarde", "Noite", "Turnos" }));
        jCPeriodo.setEnabled(false);
        jCPeriodo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCPeriodoKeyPressed(evt);
            }
        });

        jLPeriodo.setText("Período:");

        jLDtInvalida.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLDtInvalida.setForeground(new java.awt.Color(255, 0, 51));

        jLValidacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLDtInvalida1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLDtInvalida1.setForeground(new java.awt.Color(255, 0, 51));

        jCAnoCar.setEnabled(false);
        jCAnoCar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCAnoCarKeyPressed(evt);
            }
        });

        jCAnoPlaca.setEnabled(false);
        jCAnoPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCAnoPlacaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPDadosLayout = new javax.swing.GroupLayout(jPDados);
        jPDados.setLayout(jPDadosLayout);
        jPDadosLayout.setHorizontalGroup(
            jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPDadosLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLCPF, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLRG, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLCNH, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLVencimento, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLCarreta, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLModelo, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLCap, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLCod, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLDtNasc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLMotorista, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLPlaca, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLPeriodo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPDadosLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTNome, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPDadosLayout.createSequentialGroup()
                                .addComponent(jCDiaNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLSeparador1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCMesNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLSeparador2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCAnoNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLDtInvalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPDadosLayout.createSequentialGroup()
                            .addComponent(jCDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLSeparador3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jCMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLSeparador4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jCAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLDtInvalida1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jTRG, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPDadosLayout.createSequentialGroup()
                            .addComponent(jTCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLValidacao, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPDadosLayout.createSequentialGroup()
                            .addComponent(jTCNH, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLCat)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jCCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPDadosLayout.createSequentialGroup()
                            .addComponent(jTPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLAnoPl)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jCAnoPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPDadosLayout.createSequentialGroup()
                            .addComponent(jTCap, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTTurno)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jCTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jCPeriodo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPDadosLayout.createSequentialGroup()
                            .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTModelo, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTCarreta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLAnoCar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jCAnoCar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(133, 133, 133))
        );
        jPDadosLayout.setVerticalGroup(
            jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPDadosLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLCod)
                    .addComponent(jTCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLMotorista)
                    .addComponent(jTNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPDadosLayout.createSequentialGroup()
                        .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLDtNasc)
                            .addComponent(jCDiaNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLSeparador1)
                            .addComponent(jCMesNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLSeparador2)
                            .addComponent(jCAnoNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLRG)
                            .addComponent(jTRG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLDtInvalida, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLCPF)
                        .addComponent(jTCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLValidacao, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLCNH)
                    .addComponent(jTCNH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLCat)
                    .addComponent(jCCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLVencimento)
                        .addComponent(jCDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLSeparador3)
                        .addComponent(jCMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLSeparador4)
                        .addComponent(jCAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLDtInvalida1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLCarreta)
                    .addComponent(jTCarreta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLAnoCar)
                    .addComponent(jCAnoCar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLModelo)
                    .addComponent(jTModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLPlaca)
                    .addComponent(jTPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLAnoPl)
                    .addComponent(jCAnoPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLCap)
                    .addComponent(jTTurno)
                    .addComponent(jCTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLPeriodo))
                .addContainerGap(153, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPCadastroLayout = new javax.swing.GroupLayout(jPCadastro);
        jPCadastro.setLayout(jPCadastroLayout);
        jPCadastroLayout.setHorizontalGroup(
            jPCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPCadastroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPDados, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPCadastroLayout.setVerticalGroup(
            jPCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPCadastroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPDados, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTCadastro.addTab("Dados", jPCadastro);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPContato.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLTelRes.setText("Telefone Residencial:");

        jLTelCom.setText("Telefone Comercial:");

        jLCel.setText("Celular:");

        jLId.setText("ID:");

        jLTelRec.setText("Telefone Para Recado:");

        jLEmail.setText("E-mail:");

        jFTelRes.setEditable(false);
        jFTelRes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFTelResKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFTelResKeyReleased(evt);
            }
        });

        jFTelCom.setEditable(false);
        jFTelCom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFTelComKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFTelComKeyReleased(evt);
            }
        });

        jFId.setEditable(false);
        jFId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFIdKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFIdKeyReleased(evt);
            }
        });

        jFTelRec.setEditable(false);
        jFTelRec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFTelRecKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFTelRecKeyReleased(evt);
            }
        });

        jTEmail.setEditable(false);
        jTEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTEmailKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTEmailKeyReleased(evt);
            }
        });

        jFCel.setEditable(false);
        jFCel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFCelKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFCelKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPContatoLayout = new javax.swing.GroupLayout(jPContato);
        jPContato.setLayout(jPContatoLayout);
        jPContatoLayout.setHorizontalGroup(
            jPContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPContatoLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLTelCom)
                    .addComponent(jLTelRes)
                    .addComponent(jLCel)
                    .addComponent(jLId)
                    .addComponent(jLTelRec)
                    .addComponent(jLEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jFTelRec, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jFId)
                    .addComponent(jFCel)
                    .addComponent(jFTelCom)
                    .addComponent(jFTelRes, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(jTEmail))
                .addContainerGap(403, Short.MAX_VALUE))
        );
        jPContatoLayout.setVerticalGroup(
            jPContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPContatoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLTelRes)
                    .addComponent(jFTelRes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLTelCom)
                    .addComponent(jFTelCom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLCel)
                    .addComponent(jFCel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLId)
                    .addComponent(jFId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFTelRec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLTelRec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLEmail))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPEndereco.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLRua.setText("Logradouro:");

        jLNumero.setText("Número:");

        jLComplemento.setText("Complemento:");

        jLBairro.setText("Bairro:");

        jLUf.setText("UF:");

        jTRua.setEditable(false);
        jTRua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTRuaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTRuaKeyReleased(evt);
            }
        });

        jTNumero.setEditable(false);
        jTNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTNumeroKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTNumeroKeyReleased(evt);
            }
        });

        jTComplemento.setEditable(false);
        jTComplemento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTComplementoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTComplementoKeyReleased(evt);
            }
        });

        jTBairro.setEditable(false);
        jTBairro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTBairroKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTBairroKeyReleased(evt);
            }
        });

        jLCidade.setText("Cidade:");

        jTCidade.setEditable(false);
        jTCidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTCidadeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTCidadeKeyReleased(evt);
            }
        });

        jCEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO" }));
        jCEstado.setEnabled(false);
        jCEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCEstadoActionPerformed(evt);
            }
        });
        jCEstado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCEstadoKeyPressed(evt);
            }
        });

        jLPais.setText("País:");

        jTPais.setEditable(false);
        jTPais.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTPaisKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTPaisKeyReleased(evt);
            }
        });

        jLCep.setText("CEP:");

        jFTCep.setEditable(false);
        jFTCep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFTCepKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFTCepKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPEnderecoLayout = new javax.swing.GroupLayout(jPEndereco);
        jPEndereco.setLayout(jPEnderecoLayout);
        jPEnderecoLayout.setHorizontalGroup(
            jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPEnderecoLayout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addGroup(jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLNumero)
                    .addComponent(jLUf)
                    .addComponent(jLRua)
                    .addComponent(jLCidade)
                    .addComponent(jLPais))
                .addGroup(jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPEnderecoLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPEnderecoLayout.createSequentialGroup()
                                .addComponent(jTCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLBairro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPEnderecoLayout.createSequentialGroup()
                                .addComponent(jCEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLCep)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFTCep, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTRua, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPEnderecoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTPais, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPEnderecoLayout.createSequentialGroup()
                                .addComponent(jTNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLComplemento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(221, Short.MAX_VALUE))
        );
        jPEnderecoLayout.setVerticalGroup(
            jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPEnderecoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLUf)
                    .addComponent(jCEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLCep)
                    .addComponent(jFTCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPEnderecoLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLRua))
                    .addComponent(jTRua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLBairro)
                    .addComponent(jTBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLCidade)
                    .addComponent(jTCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLNumero)
                    .addComponent(jTNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLComplemento)
                    .addComponent(jTComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLPais)
                    .addComponent(jTPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel2.setText("Contato");

        jLabel3.setText("Endereço");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPContato, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPEndereco, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(1, 1, 1)
                .addComponent(jPEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPEndLayout = new javax.swing.GroupLayout(jPEnd);
        jPEnd.setLayout(jPEndLayout);
        jPEndLayout.setHorizontalGroup(
            jPEndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPEndLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPEndLayout.setVerticalGroup(
            jPEndLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPEndLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTCadastro.addTab("Endereço e Contato", jPEnd);

        JPPesquisar.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPBusca.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLPesquisa.setText("Pesquisar por:");

        jCPesquisa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Motorista", "Turno", "Placa", "Modelo", "CNH", "Data de Vencimento" }));

        jTPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTPesquisarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPBuscaLayout = new javax.swing.GroupLayout(jPBusca);
        jPBusca.setLayout(jPBuscaLayout);
        jPBuscaLayout.setHorizontalGroup(
            jPBuscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPBuscaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLPesquisa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPBuscaLayout.setVerticalGroup(
            jPBuscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPBuscaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPBuscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLPesquisa)
                    .addComponent(jCPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTPesquisa.setModel(tmMotoristas);
        jTPesquisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTPesquisaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTPesquisa);

        javax.swing.GroupLayout JPPesquisarLayout = new javax.swing.GroupLayout(JPPesquisar);
        JPPesquisar.setLayout(JPPesquisarLayout);
        JPPesquisarLayout.setHorizontalGroup(
            JPPesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPPesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPPesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)
                    .addComponent(jPBusca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        JPPesquisarLayout.setVerticalGroup(
            JPPesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPPesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout JPPesquisaLayout = new javax.swing.GroupLayout(JPPesquisa);
        JPPesquisa.setLayout(JPPesquisaLayout);
        JPPesquisaLayout.setHorizontalGroup(
            JPPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPPesquisaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        JPPesquisaLayout.setVerticalGroup(
            JPPesquisaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPPesquisaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTCadastro.addTab("Pesquisa", JPPesquisa);

        getContentPane().add(jTCadastro);
        jTCadastro.setBounds(20, 20, 770, 550);

        jPBotoes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        jBImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/print-icon.png"))); // NOI18N
        jBImprimir.setText("Imprimir");
        jBImprimir.setEnabled(false);
        jBImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBImprimirActionPerformed(evt);
            }
        });

        jBNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/add (25x25).png"))); // NOI18N
        jBNovo.setText("Novo");
        jBNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBNovoActionPerformed(evt);
            }
        });

        jBSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/disk (25x25).png"))); // NOI18N
        jBSalvar.setText("Salvar");
        jBSalvar.setEnabled(false);
        jBSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarActionPerformed(evt);
            }
        });

        jBPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Start-Menu-Search-icon (1).png"))); // NOI18N
        jBPesquisar.setText("Pesquisar");
        jBPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPesquisarActionPerformed(evt);
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

        javax.swing.GroupLayout jPBotoesLayout = new javax.swing.GroupLayout(jPBotoes);
        jPBotoes.setLayout(jPBotoesLayout);
        jPBotoesLayout.setHorizontalGroup(
            jPBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBAlterar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(jBPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(jBImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(jBExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(jBSalvar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(jBNovo, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(jBCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPBotoesLayout.setVerticalGroup(
            jPBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPBotoesLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jBNovo, javax.swing.GroupLayout.DEFAULT_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jBSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        getContentPane().add(jPBotoes);
        jPBotoes.setBounds(800, 130, 140, 320);

        jLBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/blue_waves2.jpg"))); // NOI18N
        getContentPane().add(jLBackground);
        jLBackground.setBounds(-10, -150, 970, 920);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-959)/2, (screenSize.height-626)/2, 959, 626);
    }// </editor-fold>//GEN-END:initComponents

    private void jBNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNovoActionPerformed
       habilitaDados();
        limparDados();
        jBAlterar.setEnabled(false);
        jTCadastro.setSelectedComponent(jPCadastro);
        jTNome.requestFocus();
    }//GEN-LAST:event_jBNovoActionPerformed

    private void jBSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalvarActionPerformed
       upperCase();
        validaCPF();
        
        try {
            cadastrarMotorista();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Problem no botão salvar.\nErro: "+ex);
        }
        
         nome = "";
            rg = "";
            cpf="";
            cnh="";
            carreta="";
            modelo="";
            placa="";
            capton="";
       tel ="";
       telCom="";
       cel ="";
       id = "";
       telRec="";
        rua="";
       numero="";
       bairro="";
       cidade="";
       compl="";
       cidade="";
       pais="";
       cep = "";
       
    }//GEN-LAST:event_jBSalvarActionPerformed

    private void jBPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPesquisarActionPerformed
       jTCadastro.setSelectedComponent(JPPesquisa); 
    }//GEN-LAST:event_jBPesquisarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        sair();
    }//GEN-LAST:event_formWindowClosing

    private void jBAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAlterarActionPerformed
       try {
            upperCase();
            validaCPF();
            alteraMotorista();


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema no botão alterar.\n"+ex);
        }
    }//GEN-LAST:event_jBAlterarActionPerformed

    private void jBExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBExcluirActionPerformed
        try {
            excluirMotorista();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro no botão Excluir.\n" + ex);
        }
    }//GEN-LAST:event_jBExcluirActionPerformed

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
       limparDados();
        desabilitaDados();
        jTCadastro.setSelectedComponent(jPCadastro);
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void jTPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTPesquisaMouseClicked
       linhaSelecionada(jTPesquisa);
    }//GEN-LAST:event_jTPesquisaMouseClicked

    private void jTNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTNomeKeyPressed
       
        if (evt.getKeyCode() == 10) {
            jCDiaNasc.requestFocus();
        }

        if (jTNome.getText().length() < 80) {

            nome = jTNome.getText();

        }

        if (jTNome.getText().length() > 79) {
            if (evt.getKeyCode() != 10) {
                jTNome.setText("");
                jTNome.setText(nome);
            }

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
            }

        }
    }//GEN-LAST:event_jTNomeKeyReleased

    private void jCDiaNascKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCDiaNascKeyPressed
        if (evt.getKeyCode() == 10) {
            jCMesNasc.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jTNome.requestFocus();
        }
    }//GEN-LAST:event_jCDiaNascKeyPressed

    private void jCMesNascKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCMesNascKeyPressed
        if (evt.getKeyCode() == 10) {
            jCAnoNasc.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jCDiaNasc.requestFocus();
        }
    }//GEN-LAST:event_jCMesNascKeyPressed

    private void jCAnoNascKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCAnoNascKeyPressed
       if (evt.getKeyCode() == 10) {
            jTRG.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jCMesNasc.requestFocus();
        }
    }//GEN-LAST:event_jCAnoNascKeyPressed

    private void jCDiaNascActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCDiaNascActionPerformed
        validaDataNasc();
    }//GEN-LAST:event_jCDiaNascActionPerformed

    private void jCMesNascActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCMesNascActionPerformed
        validaDataNasc();
    }//GEN-LAST:event_jCMesNascActionPerformed

    private void jCAnoNascActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCAnoNascActionPerformed
       validaDataNasc();
    }//GEN-LAST:event_jCAnoNascActionPerformed

    private void jTRGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTRGKeyPressed
       if (evt.getKeyCode() == 38) {
            jCAnoNasc.requestFocus();
        }

        if (evt.getKeyCode() == 10) {
            jTCPF.requestFocus();
        }


        if (jTRG.getText().length() < 15) {
            rg = jTRG.getText();
        }

        if (jTRG.getText().length() > 14) {
            if (evt.getKeyCode() != 10) {
                jTRG.setText("");
                jTRG.setText(rg);
            }

        }
    }//GEN-LAST:event_jTRGKeyPressed

    private void jTCPFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCPFKeyPressed
        //pula linha ao apertar shift ou alt
        if (evt.getKeyCode() == 16 || evt.getKeyCode() == 18) {
            jTCNH.requestFocus();
        }

        if (jTCPF.getText().length() <= 11) {

            jLValidacao.setVisible(false);
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                cpf = jTCPF.getText();
                validaCPF();


            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                validaCPF();
                jTCPF.setText("");
                jTCPF.setText(cpf);
            }
        } else {
            jTCPF.setText("");
            jTCPF.setText(cpf);
        }



        if (jTCPF.getText().length() > 11) {
            if (evt.getKeyCode() != 10) {
                jTCPF.setText("");
                jTCPF.setText(cpf);

            }

        }
        //pula linha ao digitar enter
        if (evt.getKeyCode() == 10) {
            jTCNH.requestFocus();
        }
        //sobe um campo ao pressionar a seta pra cima
        if (evt.getKeyCode() == 38) {
            jTRG.requestFocus();
        }
    }//GEN-LAST:event_jTCPFKeyPressed

    private void jTCPFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCPFKeyReleased
         if (jTCPF.getText().length() <= 11) {

            jLValidacao.setVisible(false);
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {//Impede que o usuário digite letras
                cpf = jTCPF.getText();
                validaCPF();//mostra mensagem se o cpf é válido ou não


            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);

                jTCPF.setText("");
                jTCPF.setText(cpf);
            }
        } else {
            jTCPF.setText("");
            jTCPF.setText(cpf);
        }


        //impede que o usuário digite mais digitos que o necessário
        if (jTCPF.getText().length() > 11) {
            if (evt.getKeyCode() != 10) {
                validaCPF();
                jTCPF.setText("");
                jTCPF.setText(cpf);

            }

        }
    }//GEN-LAST:event_jTCPFKeyReleased

    private void jTCNHKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCNHKeyPressed
       //pula linha ao apertar shift ou alt
        if (evt.getKeyCode() == 16 || evt.getKeyCode() == 18) {
            jCCat.requestFocus();
        }

        if (jTCNH.getText().length() <= 11) {

            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                cnh = jTCNH.getText();

            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                validaCPF();
                jTCNH.setText("");
                jTCNH.setText(cnh);
            }
        } else {
            jTCNH.setText("");
            jTCNH.setText(cnh);
        }



        if (jTCNH.getText().length() > 11) {
            if (evt.getKeyCode() != 10) {
                jTCNH.setText("");
                jTCNH.setText(cnh);

            }

        }
        //pula linha ao digitar enter
        if (evt.getKeyCode() == 10) {
            jCCat.requestFocus();
        }
        //sobe um campo ao pressionar a seta pra cima
        if (evt.getKeyCode() == 38) {
            jTCPF.requestFocus();
        }
    }//GEN-LAST:event_jTCNHKeyPressed

    private void jTCNHKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCNHKeyReleased
            if (jTCNH.getText().length() <= 11) {

            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                cnh = jTCNH.getText();

            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                validaCPF();
                jTCNH.setText("");
                jTCNH.setText(cnh);
            }
        } else {
            jTCNH.setText("");
            jTCNH.setText(cnh);
        }



        if (jTCNH.getText().length() > 11) {
            if (evt.getKeyCode() != 10) {
                jTCNH.setText("");
                jTCNH.setText(cnh);

            }

        }
    }//GEN-LAST:event_jTCNHKeyReleased

    private void jCCatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCCatKeyPressed
         if (evt.getKeyCode() == 10) {
            jCDia.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jTCNH.requestFocus();
        }
    }//GEN-LAST:event_jCCatKeyPressed

    private void jCDiaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCDiaKeyPressed
           if (evt.getKeyCode() == 10) {
            jCMes.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jCCat.requestFocus();
        }
    }//GEN-LAST:event_jCDiaKeyPressed

    private void jCMesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCMesKeyPressed
        if (evt.getKeyCode() == 10) {
            jCAno.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jCDia.requestFocus();
        }
    }//GEN-LAST:event_jCMesKeyPressed

    private void jCAnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCAnoKeyPressed
        if (evt.getKeyCode() == 10) {
            jTCarreta.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jCMes.requestFocus();
        }
    }//GEN-LAST:event_jCAnoKeyPressed

    private void jCDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCDiaActionPerformed
       validaDataVenc();
    }//GEN-LAST:event_jCDiaActionPerformed

    private void jCMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCMesActionPerformed
        validaDataVenc();
    }//GEN-LAST:event_jCMesActionPerformed

    private void jCAnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCAnoActionPerformed
      validaDataVenc();
    }//GEN-LAST:event_jCAnoActionPerformed

    private void jTCarretaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCarretaKeyPressed
       if (evt.getKeyCode() == 10) {
            jCAnoCar.requestFocus();
        }

        if (jTCarreta.getText().length() < 11) {

            carreta = jTCarreta.getText();

        }

        if (jTCarreta.getText().length() > 10) {
            if (evt.getKeyCode() != 10) {
                jTCarreta.setText("");
                jTCarreta.setText(carreta);
            }

        }
        
        if (evt.getKeyCode() == 38) {
            jCAno.requestFocus();
        }
    }//GEN-LAST:event_jTCarretaKeyPressed

    private void jCAnoCarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCAnoCarKeyPressed
        if (evt.getKeyCode() == 10) {
            jTModelo.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jTCarreta.requestFocus();
        }
    }//GEN-LAST:event_jCAnoCarKeyPressed

    private void jTCarretaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCarretaKeyReleased
       if (jTCarreta.getText().length() < 11) {

            carreta = jTCarreta.getText();

        }

        if (jTCarreta.getText().length() > 10) {
            if (evt.getKeyCode() != 10) {
                jTCarreta.setText("");
                jTCarreta.setText(carreta);
            }

        }
    }//GEN-LAST:event_jTCarretaKeyReleased

    private void jTModeloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTModeloKeyPressed
      if (evt.getKeyCode() == 10) {
            jTPlaca.requestFocus();
        }

        if (jTModelo.getText().length() < 30) {

            modelo = jTModelo.getText();

        }

        if (jTModelo.getText().length() > 29) {
            if (evt.getKeyCode() != 10) {
                jTModelo.setText("");
                jTModelo.setText(modelo);
            }

        }
        
        if (evt.getKeyCode() == 38) {
            jCAnoCar.requestFocus();
        }
    }//GEN-LAST:event_jTModeloKeyPressed

    private void jTModeloKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTModeloKeyReleased
        if (jTModelo.getText().length() < 30) {

            modelo = jTModelo.getText();

        }

        if (jTModelo.getText().length() > 29) {
            if (evt.getKeyCode() != 10) {
                jTModelo.setText("");
                jTModelo.setText(modelo);
            }

        }
    }//GEN-LAST:event_jTModeloKeyReleased

    private void jTPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTPlacaKeyPressed
         if (evt.getKeyCode() == 10) {
            jCAnoPlaca.requestFocus();
        }

        if (jTPlaca.getText().length() < 11) {

            placa = jTPlaca.getText();

        }

        if (jTPlaca.getText().length() > 10) {
            if (evt.getKeyCode() != 10) {
                jTPlaca.setText("");
                jTPlaca.setText(placa);
            }

        }
        
        if (evt.getKeyCode() == 38) {
            jTModelo.requestFocus();
        }
    }//GEN-LAST:event_jTPlacaKeyPressed

    private void jTPlacaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTPlacaKeyReleased
       if (jTPlaca.getText().length() < 11) {

            placa = jTPlaca.getText();

        }

        if (jTPlaca.getText().length() > 10) {
            if (evt.getKeyCode() != 10) {
                jTPlaca.setText("");
                jTPlaca.setText(placa);
            }

        }
    }//GEN-LAST:event_jTPlacaKeyReleased

    private void jCAnoPlacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCAnoPlacaKeyPressed
           if (evt.getKeyCode() == 10) {
            jTCap.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jCAnoPlaca.requestFocus();
        }
    }//GEN-LAST:event_jCAnoPlacaKeyPressed
int ponto; //Variável para controlar o número de vezes que o ponto é digitado
    private void jTCapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCapKeyPressed
       if (evt.getKeyCode() == 10 || evt.getKeyCode() == 16 || evt.getKeyCode() == 18) {
            jCTurno.requestFocus();
        }

       
        if (jTCap.getText().length() < 10) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10
                    || evt.getKeyCode() == 46) {

                capton = jTCap.getText();

                if (evt.getKeyCode() == 46) {
                    ponto++;
                }

                if (jTCap.getText().length() == 0) {
                    if (evt.getKeyCode() == 46) {
                        ponto = 2;
                    }
                }

                if (evt.getKeyCode() == 46 && ponto >= 2) {
                    JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    jTCap.setText("");
                    jTCap.setText(capton);
                }

                if (!jTCap.getText().contains(".")) {
                    ponto = 0;
                }


            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas número.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jTCap.setText("");
                jTCap.setText(capton);
            }

        } else {
            jTCap.setText("");
            jTCap.setText(capton);
        }

        if (jTCap.getText().length() > 9) {
            if (evt.getKeyCode() != 10) {
                jTCap.setText("");
                jTCap.setText(capton);
            }

        }


        if (evt.getKeyCode() == 38) {
            jCAnoPlaca.requestFocus();
        }
    }//GEN-LAST:event_jTCapKeyPressed

    private void jTCapKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCapKeyReleased
        if (jTCap.getText().length() < 10) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10
                    || evt.getKeyCode() == 46) {

                capton = jTCap.getText();

                if (evt.getKeyCode() == 46) {
                    ponto++;
                }

                if (jTCap.getText().length() == 0) {
                    if (evt.getKeyCode() == 46) {
                        ponto = 2;
                    }
                }

                if (evt.getKeyCode() == 46 && ponto >= 2) {
                    JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    jTCap.setText("");
                    jTCap.setText(capton);
                }

                if (!jTCap.getText().contains(".")) {
                    ponto = 0;
                }


            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas número.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jTCap.setText("");
                jTCap.setText(capton);
            }

        } else {
            jTCap.setText("");
            jTCap.setText(capton);
        }

        if (jTCap.getText().length() > 9) {
            if (evt.getKeyCode() != 10) {
                jTCap.setText("");
                jTCap.setText(capton);
            }

        }
    }//GEN-LAST:event_jTCapKeyReleased

    private void jCTurnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCTurnoKeyPressed
          if (evt.getKeyCode() == 10) {
            jCPeriodo.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jTCap.requestFocus();
        }
    }//GEN-LAST:event_jCTurnoKeyPressed

    private void jCPeriodoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCPeriodoKeyPressed
         if (evt.getKeyCode() == 10) {
             jTCadastro.setSelectedComponent(jPEnd);
            jFTelRes.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jCTurno.requestFocus();
        }
    }//GEN-LAST:event_jCPeriodoKeyPressed

    private void jFTelResKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFTelResKeyPressed
        if (evt.getKeyCode() == 16 || evt.getKeyCode() == 18) {
            jCEstado.requestFocus();
        }
        
        if (jFTelRes.getText().length() <= 14) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                tel = jFTelRes.getText();
                if (jFTelRes.getText().length() == 0) {
                    if (evt.getKeyCode() != 8) {
                        tel = jFTelRes.getText();
                        jFTelRes.setText("(" + tel);
                    }

                } else if (jFTelRes.getText().length() == 4 && !jFTelRes.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        tel = jFTelRes.getText();
                        jFTelRes.setText(tel + ")");
                    }
                } else if (jFTelRes.getText().length() == 5 && jFTelRes.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        tel = jFTelRes.getText();
                        jFTelRes.setText(tel);
                    }
                } else if (jFTelRes.getText().length() == 9) {
                    if (evt.getKeyCode() != 8) {
                        tel = jFTelRes.getText();
                        jFTelRes.setText(tel + "-");
                    }
                }
                tel = jFTelRes.getText();
            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFTelRes.setText("");
                jFTelRes.setText(tel);
            }
        } else {
            jFTelRes.setText("");
            jFTelRes.setText(tel);
        }




        if (jFTelRes.getText().length() > 14 || jFTelRes.getText().length() > 0 && jFTelRes.getText().charAt(0) == '(') {
            if (evt.getKeyCode() != 10 || evt.getKeyCode() != 8) {
                jFTelRes.setText("");
                jFTelRes.setText(tel);
            }

        }

        if (evt.getKeyCode() == 10) {
            jFTelCom.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
             jTNome.requestFocus();
            jTCadastro.setSelectedComponent(jPCadastro);
           
        }
    }//GEN-LAST:event_jFTelResKeyPressed

    private void jFTelResKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFTelResKeyReleased
       if (jFTelRes.getText().length() <= 14) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                tel = jFTelRes.getText();
                if (jFTelRes.getText().length() == 0) {
                    if (evt.getKeyCode() != 8) {
                        tel = jFTelRes.getText();
                        jFTelRes.setText("(" + tel);
                    }

                } else if (jFTelRes.getText().length() == 4 && !jFTelRes.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        tel = jFTelRes.getText();
                        jFTelRes.setText(tel + ")");
                    }
                } else if (jFTelRes.getText().length() == 5 && jFTelRes.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        tel = jFTelRes.getText();
                        jFTelRes.setText(tel);
                    }
                } else if (jFTelRes.getText().length() == 9) {
                    if (evt.getKeyCode() != 8) {
                        tel = jFTelRes.getText();
                        jFTelRes.setText(tel + "-");
                    }
                }
                tel = jFTelRes.getText();
            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFTelRes.setText("");
                jFTelRes.setText(tel);
            }
        } else {
            jFTelRes.setText("");
            jFTelRes.setText(tel);
        }




        if (jFTelRes.getText().length() > 14 || jFTelRes.getText().length() > 0 && jFTelRes.getText().charAt(0) == '(') {
            if (evt.getKeyCode() != 10 || evt.getKeyCode() != 8) {
                jFTelRes.setText("");
                jFTelRes.setText(tel);
            }

        }

    }//GEN-LAST:event_jFTelResKeyReleased

    private void jFTelComKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFTelComKeyPressed
        if (evt.getKeyCode() == 16 || evt.getKeyCode() == 18) {
            jCEstado.requestFocus();
        }
        if (jFTelCom.getText().length() <= 14) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                telCom = jFTelCom.getText();
                if (jFTelCom.getText().length() == 0) {
                    if (evt.getKeyCode() != 8) {
                        telCom = jFTelCom.getText();
                        jFTelCom.setText("(" + telCom);
                    }

                } else if (jFTelCom.getText().length() == 4 && !jFTelCom.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        telCom = jFTelCom.getText();
                        jFTelCom.setText(telCom + ")");
                    }
                } else if (jFTelCom.getText().length() == 5 && jFTelCom.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        telCom = jFTelCom.getText();
                        jFTelCom.setText(telCom);
                    }
                } else if (jFTelCom.getText().length() == 9) {
                    if (evt.getKeyCode() != 8) {
                        telCom = jFTelCom.getText();
                        jFTelCom.setText(telCom + "-");
                    }
                }
                telCom = jFTelCom.getText();
            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFTelCom.setText("");
                jFTelCom.setText(telCom);
            }
        } else {
            jFTelCom.setText("");
            jFTelCom.setText(telCom);
        }




        if (jFTelCom.getText().length() > 14 || jFTelCom.getText().length() > 0 && jFTelCom.getText().charAt(0) == '(') {
            if (evt.getKeyCode() != 10 || evt.getKeyCode() != 8) {
                jFTelCom.setText("");
                jFTelCom.setText(telCom);
            }

        }

        if (evt.getKeyCode() == 10) {
            jFCel.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jFTelRes.requestFocus();
        }
    }//GEN-LAST:event_jFTelComKeyPressed

    private void jFTelComKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFTelComKeyReleased
            if (jFTelCom.getText().length() <= 14) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                telCom = jFTelCom.getText();
                if (jFTelCom.getText().length() == 0) {
                    if (evt.getKeyCode() != 8) {
                        telCom = jFTelCom.getText();
                        jFTelCom.setText("(" + telCom);
                    }

                } else if (jFTelCom.getText().length() == 4 && !jFTelCom.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        telCom = jFTelCom.getText();
                        jFTelCom.setText(telCom + ")");
                    }
                } else if (jFTelCom.getText().length() == 5 && jFTelCom.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        telCom = jFTelCom.getText();
                        jFTelCom.setText(telCom);
                    }
                } else if (jFTelCom.getText().length() == 9) {
                    if (evt.getKeyCode() != 8) {
                        telCom = jFTelCom.getText();
                        jFTelCom.setText(telCom + "-");
                    }
                }
                telCom = jFTelCom.getText();
            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFTelCom.setText("");
                jFTelCom.setText(telCom);
            }
        } else {
            jFTelCom.setText("");
            jFTelCom.setText(telCom);
        }




        if (jFTelCom.getText().length() > 14 || jFTelCom.getText().length() > 0 && jFTelCom.getText().charAt(0) == '(') {
            if (evt.getKeyCode() != 10 || evt.getKeyCode() != 8) {
                jFTelCom.setText("");
                jFTelCom.setText(telCom);
            }

        }
    }//GEN-LAST:event_jFTelComKeyReleased

    private void jFCelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFCelKeyPressed
         if (evt.getKeyCode() == 16 || evt.getKeyCode() == 18) {
            jCEstado.requestFocus();
        }
        if (jFCel.getText().length() <= 15) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                cel = jFCel.getText();
                if (jFCel.getText().length() == 0) {
                    if (evt.getKeyCode() != 8) {
                        cel = jFCel.getText();
                        jFCel.setText("(" + cel);
                    }

                } else if (jFCel.getText().length() == 4 && !jFCel.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        cel = jFCel.getText();
                        jFCel.setText(cel + ")");
                    }
                } else if (jFCel.getText().length() == 5 && jFCel.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        cel = jFCel.getText();
                        jFCel.setText(cel);
                    }
                } else if (jFCel.getText().length() == 9) {
                    if (evt.getKeyCode() != 8) {
                        cel = jFCel.getText();
                        jFCel.setText(cel + "-");
                    }
                }
                cel = jFCel.getText();
            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFCel.setText("");
                jFCel.setText(cel);
            }
        } else {
            jFCel.setText("");
            jFCel.setText(cel);
        }




        if (jFCel.getText().length() > 15 || jFCel.getText().length() > 0 && jFCel.getText().charAt(0) == '(') {
            if (evt.getKeyCode() != 10 || evt.getKeyCode() != 8) {
                jFCel.setText("");
                jFCel.setText(cel);
            }

        }

        if (evt.getKeyCode() == 10) {
            jFId.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jFTelCom.requestFocus();
        }
    }//GEN-LAST:event_jFCelKeyPressed

    private void jFCelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFCelKeyReleased
       if (jFCel.getText().length() <= 15) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                cel = jFCel.getText();
                if (jFCel.getText().length() == 0) {
                    if (evt.getKeyCode() != 8) {
                        cel = jFCel.getText();
                        jFCel.setText("(" + cel);
                    }

                } else if (jFCel.getText().length() == 4 && !jFCel.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        cel = jFCel.getText();
                        jFCel.setText(cel + ")");
                    }
                } else if (jFCel.getText().length() == 5 && jFCel.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        cel = jFCel.getText();
                        jFCel.setText(cel);
                    }
                } else if (jFCel.getText().length() == 9) {
                    if (evt.getKeyCode() != 8) {
                        cel = jFCel.getText();
                        jFCel.setText(cel + "-");
                    }
                }
                cel = jFCel.getText();
            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFCel.setText("");
                jFCel.setText(cel);
            }
        } else {
            jFCel.setText("");
            jFCel.setText(cel);
        }




        if (jFCel.getText().length() > 15 || jFCel.getText().length() > 0 && jFCel.getText().charAt(0) == '(') {
            if (evt.getKeyCode() != 10 || evt.getKeyCode() != 8) {
                jFCel.setText("");
                jFCel.setText(cel);
            }

        }
    }//GEN-LAST:event_jFCelKeyReleased

    private void jFIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFIdKeyPressed
        if (evt.getKeyCode() == 18) {
            jCEstado.requestFocus();
        }
        if (jFId.getText().length() < 20) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() == 10 || evt.getKeyCode() == 38 || evt.getKeyCode() == 16) {
                id = jFId.getText();
            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFId.setText("");
                jFId.setText(id);
            }
        }

        if (jFId.getText().length() > 19) {
            if (evt.getKeyCode() != 10) {
                jFId.setText("");
                jFId.setText(id);
            }
        }


        if (evt.getKeyCode() == 10) {
            jFTelRec.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jFCel.requestFocus();
        }
    }//GEN-LAST:event_jFIdKeyPressed

    private void jFIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFIdKeyReleased
        if (jFId.getText().length() < 20) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() == 10 || evt.getKeyCode() == 38 || evt.getKeyCode() == 16) {
                id = jFId.getText();
            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFId.setText("");
                jFId.setText(id);
            }
        }

        if (jFId.getText().length() > 19) {
            if (evt.getKeyCode() != 10) {
                jFId.setText("");
                jFId.setText(id);
            }
        }

    }//GEN-LAST:event_jFIdKeyReleased

    private void jFTelRecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFTelRecKeyPressed
       if (evt.getKeyCode() == 16 || evt.getKeyCode() == 18) {
            jCEstado.requestFocus();
        }
        if (jFTelRec.getText().length() <= 14) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                telRec = jFTelRec.getText();
                if (jFTelRec.getText().length() == 0) {
                    if (evt.getKeyCode() != 8) {
                        telRec = jFTelRec.getText();
                        jFTelRec.setText("(" + telRec);
                    }

                } else if (jFTelRec.getText().length() == 4 && !jFTelRec.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        telRec = jFTelRec.getText();
                        jFTelRec.setText(telRec + ")");
                    }
                } else if (jFTelRec.getText().length() == 5 && jFTelRec.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        telRec = jFTelRec.getText();
                        jFTelRec.setText(telRec);
                    }
                } else if (jFTelRec.getText().length() == 9) {
                    if (evt.getKeyCode() != 8) {
                        telRec = jFTelRec.getText();
                        jFTelRec.setText(telRec + "-");
                    }
                }
                telRec = jFTelRec.getText();
            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFTelRec.setText("");
                jFTelRec.setText(telRec);
            }
        } else {
            jFTelRec.setText("");
            jFTelRec.setText(telRec);
        }




        if (jFTelRec.getText().length() > 14 || jFTelRec.getText().length() > 0 && jFTelRec.getText().charAt(0) == '(') {
            if (evt.getKeyCode() != 10 || evt.getKeyCode() != 8) {
                jFTelRec.setText("");
                jFTelRec.setText(telRec);
            }

        }

        if (evt.getKeyCode() == 10) {

            jTEmail.requestFocus();
        }

        if (evt.getKeyCode() == 38) {

            jFId.requestFocus();
        }
    }//GEN-LAST:event_jFTelRecKeyPressed

    private void jFTelRecKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFTelRecKeyReleased
           if (jFTelRec.getText().length() <= 14) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40 || evt.getKeyCode() == 10) {
                telRec = jFTelRec.getText();
                if (jFTelRec.getText().length() == 0) {
                    if (evt.getKeyCode() != 8) {
                        telRec = jFTelRec.getText();
                        jFTelRec.setText("(" + telRec);
                    }

                } else if (jFTelRec.getText().length() == 4 && !jFTelRec.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        telRec = jFTelRec.getText();
                        jFTelRec.setText(telRec + ")");
                    }
                } else if (jFTelRec.getText().length() == 5 && jFTelRec.getText().contains(")")) {
                    if (evt.getKeyCode() != 8) {
                        telRec = jFTelRec.getText();
                        jFTelRec.setText(telRec);
                    }
                } else if (jFTelRec.getText().length() == 9) {
                    if (evt.getKeyCode() != 8) {
                        telRec = jFTelRec.getText();
                        jFTelRec.setText(telRec + "-");
                    }
                }
                telRec = jFTelRec.getText();
            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFTelRec.setText("");
                jFTelRec.setText(telRec);
            }
        } else {
            jFTelRec.setText("");
            jFTelRec.setText(telRec);
        }




        if (jFTelRec.getText().length() > 14 || jFTelRec.getText().length() > 0 && jFTelRec.getText().charAt(0) == '(') {
            if (evt.getKeyCode() != 10 || evt.getKeyCode() != 8) {
                jFTelRec.setText("");
                jFTelRec.setText(telRec);
            }

        }
    }//GEN-LAST:event_jFTelRecKeyReleased

    private void jTEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTEmailKeyPressed
         if (evt.getKeyCode() == 10) {
            jCEstado.requestFocus();
        }

        if (jTEmail.getText().length() < 50) {
            email = jTEmail.getText();
        }


        if (jTEmail.getText().length() > 49) {
            if (evt.getKeyCode() != 10) {
                jTEmail.setText("");
                jTEmail.setText(email);
            }

        }


        if (evt.getKeyCode() == 38) {

            jFTelRec.requestFocus();
        }
    }//GEN-LAST:event_jTEmailKeyPressed

    private void jTEmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTEmailKeyReleased
       if (jTEmail.getText().length() < 50) {
            email = jTEmail.getText();
        }


        if (jTEmail.getText().length() > 49) {
            if (evt.getKeyCode() != 10) {
                jTEmail.setText("");
                jTEmail.setText(email);
            }

        }
    }//GEN-LAST:event_jTEmailKeyReleased

    private void jCEstadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCEstadoKeyPressed
         if (evt.getKeyCode() == 10) {
            jFTCep.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jTEmail.requestFocus();
        }
    }//GEN-LAST:event_jCEstadoKeyPressed

    private void jFTCepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFTCepKeyPressed
       if (evt.getKeyCode() == 16 || evt.getKeyCode() == 18) {
            jTRua.requestFocus();
        }

        if (jFTCep.getText().length() <= 9) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() == 10 || evt.getKeyCode() == 38) {
                //cpf = jFTCep.getText();

                if (jFTCep.getText().length() == 5) {
                    if (evt.getKeyCode() != 8) {
                        cep = jFTCep.getText();
                        jFTCep.setText(cep + "-");
                    }
                } else {
                    cep = jFTCep.getText();
                }


            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFTCep.setText("");
                jFTCep.setText(cep);
            }
        }
        
      

        if (jFTCep.getText().length() > 9) {
            if (evt.getKeyCode() != 10 || evt.getKeyCode() != 8) {
                jFTCep.setText("");
                jFTCep.setText(cep);
            }

        }


        if (evt.getKeyCode() == 10) {
            jTRua.requestFocus();
        }

        if (evt.getKeyCode() == 38) {
            jCEstado.requestFocus();
        }
    }//GEN-LAST:event_jFTCepKeyPressed

    private void jFTCepKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFTCepKeyReleased
       if (jFTCep.getText().length() <= 9) {
            if (evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                    || evt.getKeyCode() == 8 || evt.getKeyCode() == 10 || evt.getKeyCode() == 38) {
                //cpf = jFTCep.getText();

                if (jFTCep.getText().length() == 5) {
                    if (evt.getKeyCode() != 8) {
                        cep = jFTCep.getText();
                        jFTCep.setText(cep + "-");
                    }
                } else {
                    cep = jFTCep.getText();
                }


            } else {
                JOptionPane.showMessageDialog(this, "Digite apenas números.", "Atenção", JOptionPane.WARNING_MESSAGE);
                jFTCep.setText("");
                jFTCep.setText(cep);
            }
        }
        
        if(jFTCep.getText().length()==9){
             
           preencheEnd();
        }
      

        if (jFTCep.getText().length() > 9) {
            if (evt.getKeyCode() != 10 || evt.getKeyCode() != 8) {
                jFTCep.setText("");
                jFTCep.setText(cep);
            }

        }

    }//GEN-LAST:event_jFTCepKeyReleased

    private void jTRuaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTRuaKeyPressed
        if (evt.getKeyCode() == 10) {
            jTCidade.requestFocus();
        }

        if (jTRua.getText().length() < 60) {
            rua = jTRua.getText();
        }


        if (jTRua.getText().length() > 59) {
            if (evt.getKeyCode() != 10) {
                jTRua.setText("");
                jTRua.setText(rua);
            }

        }

        if (evt.getKeyCode() == 38) {
            jFTCep.requestFocus();
        }
    }//GEN-LAST:event_jTRuaKeyPressed

    private void jTRuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTRuaKeyReleased
        if (jTRua.getText().length() < 60) {
            rua = jTRua.getText();
        }


        if (jTRua.getText().length() > 59) {
            if (evt.getKeyCode() != 10) {
                jTRua.setText("");
                jTRua.setText(rua);
            }

        }
    }//GEN-LAST:event_jTRuaKeyReleased

    private void jTCidadeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCidadeKeyPressed
      if (evt.getKeyCode() == 10) {
            jTBairro.requestFocus();
        }

        if (jTCidade.getText().length() < 50) {
            cidade = jTCidade.getText();
        }


        if (jTCidade.getText().length() > 49) {
            if (evt.getKeyCode() != 10) {
                jTCidade.setText("");
                jTCidade.setText(cidade);
            }

        }

        if (evt.getKeyCode() == 38) {
            jTRua.requestFocus();
        }
    }//GEN-LAST:event_jTCidadeKeyPressed

    private void jTCidadeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTCidadeKeyReleased
        if (jTCidade.getText().length() < 50) {
            cidade = jTCidade.getText();
        }


        if (jTCidade.getText().length() > 49) {
            if (evt.getKeyCode() != 10) {
                jTCidade.setText("");
                jTCidade.setText(cidade);
            }

        }
    }//GEN-LAST:event_jTCidadeKeyReleased

    private void jTBairroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTBairroKeyPressed
         if (evt.getKeyCode() == 10) {
            jTNumero.requestFocus();
        }

        if (jTBairro.getText().length() < 50) {
            bairro = jTBairro.getText();
        }


        if (jTBairro.getText().length() > 49) {
            if (evt.getKeyCode() != 10) {
                jTBairro.setText("");
                jTBairro.setText(bairro);
            }

        }

        if (evt.getKeyCode() == 38) {
            jTCidade.requestFocus();
        }
    }//GEN-LAST:event_jTBairroKeyPressed

    private void jTBairroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTBairroKeyReleased
        if (jTBairro.getText().length() < 50) {
            bairro = jTBairro.getText();
        }


        if (jTBairro.getText().length() > 49) {
            if (evt.getKeyCode() != 10) {
                jTBairro.setText("");
                jTBairro.setText(bairro);
            }

        }
    }//GEN-LAST:event_jTBairroKeyReleased

    private void jTNumeroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTNumeroKeyPressed
       if (evt.getKeyCode() == 10) {
            jTComplemento.requestFocus();
        }

        if (jTNumero.getText().length() < 10) {
            numero = jTNumero.getText();
        }


        if (jTNumero.getText().length() > 9) {
            if (evt.getKeyCode() != 10) {
                jTNumero.setText("");
                jTNumero.setText(numero);
            }

        }



        if (evt.getKeyCode() == 38) {
            jTBairro.requestFocus();
        }
    }//GEN-LAST:event_jTNumeroKeyPressed

    private void jTNumeroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTNumeroKeyReleased
        if (jTNumero.getText().length() < 10) {
            numero = jTNumero.getText();
        }


        if (jTNumero.getText().length() > 9) {
            if (evt.getKeyCode() != 10) {
                jTNumero.setText("");
                jTNumero.setText(numero);
            }

        }
    }//GEN-LAST:event_jTNumeroKeyReleased

    private void jTComplementoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTComplementoKeyPressed
        if (evt.getKeyCode() == 10) {
            jTPais.requestFocus();
        }

        if (jTComplemento.getText().length() < 10) {
            compl = jTComplemento.getText();
        }


        if (jTComplemento.getText().length() > 9) {
            if (evt.getKeyCode() != 10) {
                jTComplemento.setText("");
                jTComplemento.setText(compl);
            }

        }

        if (evt.getKeyCode() == 38) {
            jTNumero.requestFocus();
        }
    }//GEN-LAST:event_jTComplementoKeyPressed

    private void jTComplementoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTComplementoKeyReleased
       if (jTComplemento.getText().length() < 10) {
            compl = jTComplemento.getText();
        }


        if (jTComplemento.getText().length() > 9) {
            if (evt.getKeyCode() != 10) {
                jTComplemento.setText("");
                jTComplemento.setText(compl);
            }

        }
    }//GEN-LAST:event_jTComplementoKeyReleased

    private void jTPaisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTPaisKeyPressed
       if (jTPais.getText().length() < 50) {
            pais = jTPais.getText();
        }


        if (jTPais.getText().length() > 49) {
            if (evt.getKeyCode() != 10) {
                jTPais.setText("");
                jTPais.setText(pais);
            }

        }

        if (evt.getKeyCode() == 38) {
           jTComplemento.requestFocus();
        }
    }//GEN-LAST:event_jTPaisKeyPressed

    private void jTPaisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTPaisKeyReleased
        if (jTPais.getText().length() < 50) {
            pais = jTPais.getText();
        }


        if (jTPais.getText().length() > 49) {
            if (evt.getKeyCode() != 10) {
                jTPais.setText("");
                jTPais.setText(pais);
            }

        }
    }//GEN-LAST:event_jTPaisKeyReleased

    private void jCEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCEstadoActionPerformed
          preencheEnd();
    }//GEN-LAST:event_jCEstadoActionPerformed

    private void jTPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTPesquisarKeyReleased
         if (evt.getKeyCode() >= 60 && evt.getKeyCode() <= 90 || evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40
                || evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57 || evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105
                || evt.getKeyCode() == 8 || evt.getKeyCode() == 10) {
            try {
                listarMotoristas();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Problemas no campo de pesquisa.\n" + ex);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nenhhum motorista encontrado.\n" + e);
            }
            jTPesquisar.requestFocus();
        }
    }//GEN-LAST:event_jTPesquisarKeyReleased

    private void jTRGKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTRGKeyReleased
         if (jTRG.getText().length() < 15) {
            rg = jTRG.getText();
        }

        if (jTRG.getText().length() > 14) {
            if (evt.getKeyCode() != 10) {
                jTRG.setText("");
                jTRG.setText(rg);
            }

        }
    }//GEN-LAST:event_jTRGKeyReleased

    private void jBImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBImprimirActionPerformed
       if (verificaDados()) {
           impressaoMotorista();
        } else {
            JOptionPane.showMessageDialog(null, "Todos os campos obrigatórios devem ser preenchidos para impressão.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jBImprimirActionPerformed

    
    
    
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
            java.util.logging.Logger.getLogger(JDCadastros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDCadastros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDCadastros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDCadastros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                JDCadastros dialog = null;
                try {
                    dialog = new JDCadastros(new javax.swing.JFrame(), true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"Erro ao abrir formulário de cadastro de motoristas.\nErro: "+ex);
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
    private javax.swing.JPanel JPPesquisa;
    private javax.swing.JPanel JPPesquisar;
    private javax.swing.JButton jBAlterar;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBExcluir;
    private javax.swing.JButton jBImprimir;
    private javax.swing.JButton jBNovo;
    private javax.swing.JButton jBPesquisar;
    private javax.swing.JButton jBSalvar;
    private javax.swing.JComboBox jCAno;
    private javax.swing.JComboBox jCAnoCar;
    private javax.swing.JComboBox jCAnoNasc;
    private javax.swing.JComboBox jCAnoPlaca;
    private javax.swing.JComboBox jCCat;
    private javax.swing.JComboBox jCDia;
    private javax.swing.JComboBox jCDiaNasc;
    private javax.swing.JComboBox jCEstado;
    private javax.swing.JComboBox jCMes;
    private javax.swing.JComboBox jCMesNasc;
    private javax.swing.JComboBox jCPeriodo;
    private javax.swing.JComboBox jCPesquisa;
    private javax.swing.JComboBox jCTurno;
    private javax.swing.JFormattedTextField jFCel;
    private javax.swing.JFormattedTextField jFId;
    private javax.swing.JFormattedTextField jFTCep;
    private javax.swing.JFormattedTextField jFTelCom;
    private javax.swing.JFormattedTextField jFTelRec;
    private javax.swing.JFormattedTextField jFTelRes;
    private javax.swing.JLabel jLAnoCar;
    private javax.swing.JLabel jLAnoPl;
    private javax.swing.JLabel jLBackground;
    private javax.swing.JLabel jLBairro;
    private javax.swing.JLabel jLCNH;
    private javax.swing.JLabel jLCPF;
    private javax.swing.JLabel jLCap;
    private javax.swing.JLabel jLCarreta;
    private javax.swing.JLabel jLCat;
    private javax.swing.JLabel jLCel;
    private javax.swing.JLabel jLCep;
    private javax.swing.JLabel jLCidade;
    private javax.swing.JLabel jLCod;
    private javax.swing.JLabel jLComplemento;
    private javax.swing.JLabel jLDtInvalida;
    private javax.swing.JLabel jLDtInvalida1;
    private javax.swing.JLabel jLDtNasc;
    private javax.swing.JLabel jLEmail;
    private javax.swing.JLabel jLId;
    private javax.swing.JLabel jLModelo;
    private javax.swing.JLabel jLMotorista;
    private javax.swing.JLabel jLNumero;
    private javax.swing.JLabel jLPais;
    private javax.swing.JLabel jLPeriodo;
    private javax.swing.JLabel jLPesquisa;
    private javax.swing.JLabel jLPlaca;
    private javax.swing.JLabel jLRG;
    private javax.swing.JLabel jLRua;
    private javax.swing.JLabel jLSeparador1;
    private javax.swing.JLabel jLSeparador2;
    private javax.swing.JLabel jLSeparador3;
    private javax.swing.JLabel jLSeparador4;
    private javax.swing.JLabel jLTelCom;
    private javax.swing.JLabel jLTelRec;
    private javax.swing.JLabel jLTelRes;
    private javax.swing.JLabel jLUf;
    private javax.swing.JLabel jLValidacao;
    private javax.swing.JLabel jLVencimento;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPBotoes;
    private javax.swing.JPanel jPBusca;
    private javax.swing.JPanel jPCadastro;
    private javax.swing.JPanel jPContato;
    private javax.swing.JPanel jPDados;
    private javax.swing.JPanel jPEnd;
    private javax.swing.JPanel jPEndereco;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTBairro;
    private javax.swing.JTextField jTCNH;
    private javax.swing.JTextField jTCPF;
    private javax.swing.JTabbedPane jTCadastro;
    private javax.swing.JTextField jTCap;
    private javax.swing.JTextField jTCarreta;
    private javax.swing.JTextField jTCidade;
    private javax.swing.JTextField jTCodigo;
    private javax.swing.JTextField jTComplemento;
    private javax.swing.JTextField jTEmail;
    private javax.swing.JTextField jTModelo;
    private javax.swing.JTextField jTNome;
    private javax.swing.JTextField jTNumero;
    private javax.swing.JTextField jTPais;
    private javax.swing.JTable jTPesquisa;
    private javax.swing.JTextField jTPesquisar;
    private javax.swing.JTextField jTPlaca;
    private javax.swing.JTextField jTRG;
    private javax.swing.JTextField jTRua;
    private javax.swing.JLabel jTTurno;
    // End of variables declaration//GEN-END:variables
}
