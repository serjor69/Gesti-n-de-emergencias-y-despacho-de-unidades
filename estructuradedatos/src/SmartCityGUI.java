package src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class SmartCityGUI extends JFrame {
    // Estructuras de Datos
    private InventarioMedicinas stockBST = new InventarioMedicinas();
    private GestionLlamadas colaEmergencias = new GestionLlamadas();
    private HistorialDespachos historialPila = new HistorialDespachos();
    private RedEmergencias911 redBogota = new RedEmergencias911();

    // Paleta de Colores Neo-Bogotá
    private final Color NEGRO_ABISAL = new Color(10, 10, 12);
    private final Color GRIS_PANEL = new Color(25, 25, 30);
    private final Color CIAN_ELECTRICO = new Color(0, 215, 255);
    private final Color AMARILLO_BOGOTA = new Color(255, 210, 0);
    private final Color ROJO_ALERTA = new Color(220, 20, 30);

    // Componentes Visuales
    private JTextArea txtRadar, txtLogGeneral, txtInventarioRef, txtPlanos;
    private JTextField txtCodMedicina;
    private JComboBox<String> cbLocalidades, cbTipoUnidad;
    private JLabel lblStatus;

    public SmartCityGUI() {
        // --- CONFIGURACIÓN DE VENTANA ---
        setTitle("SISTEMA DE RESPUESTA INTEGRADA 911 - BOGOTÁ D.C.");
        setSize(1350, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(NEGRO_ABISAL);

        // --- 1. CABECERA (STATUS BAR) ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(ROJO_ALERTA);
        pnlHeader.setPreferredSize(new Dimension(100, 65));
        JLabel lblTitulo = new JLabel("  ☢ CENTRO DE COMANDO Y CONTROL (CCC) - DISTRITO CAPITAL");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pnlHeader.add(lblTitulo, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- 2. PANEL IZQUIERDO: MEDICAMENTOS (BST) ---
        JPanel pnlIzquierdo = new JPanel(new BorderLayout(10, 10));
        pnlIzquierdo.setPreferredSize(new Dimension(340, 0));
        pnlIzquierdo.setBackground(GRIS_PANEL);
        pnlIzquierdo.setBorder(new TitledBorder(new LineBorder(CIAN_ELECTRICO, 2), " STOCK MEDICAMENTOS (BST) ", 
                               TitledBorder.LEFT, TitledBorder.TOP, null, CIAN_ELECTRICO));

        txtInventarioRef = new JTextArea();
        txtInventarioRef.setBackground(Color.BLACK);
        txtInventarioRef.setForeground(Color.WHITE);
        txtInventarioRef.setFont(new Font("Consolas", Font.BOLD, 14));
        txtInventarioRef.setEditable(false);
        
        // --- PRE-CARGA DE MEDICAMENTOS ---
        cargarMedicamentosIniciales();

        JPanel pnlInputBST = new JPanel(new GridLayout(4, 1, 5, 5));
        pnlInputBST.setOpaque(false);
        txtCodMedicina = new JTextField();
        JButton btnAdd = new JButton("VINCULAR CÓDIGO");
        JButton btnSearch = new JButton("RASTREO EN ÁRBOL");
        
        JLabel lblRef = new JLabel("REF. MEDICAMENTO (ID):", JLabel.CENTER);
        lblRef.setForeground(Color.WHITE);
        pnlInputBST.add(lblRef);
        pnlInputBST.add(txtCodMedicina);
        pnlInputBST.add(btnAdd);
        pnlInputBST.add(btnSearch);

        pnlIzquierdo.add(new JScrollPane(txtInventarioRef), BorderLayout.CENTER);
        pnlIzquierdo.add(pnlInputBST, BorderLayout.SOUTH);
        add(pnlIzquierdo, BorderLayout.WEST);

        // --- 3. PANEL CENTRAL: RADAR Y DESPACHO ---
        JPanel pnlCentro = new JPanel(new GridLayout(2, 1, 15, 15));
        pnlCentro.setOpaque(false);

        // RADAR FIFO (Muestra si es ambulancia o bomberos)
        txtRadar = new JTextArea();
        txtRadar.setBackground(Color.BLACK);
        txtRadar.setForeground(new Color(0, 255, 150));
        txtRadar.setFont(new Font("Monospaced", Font.BOLD, 16));
        txtRadar.setEditable(false);
        JScrollPane scrollRadar = new JScrollPane(txtRadar);
        scrollRadar.setBorder(BorderFactory.createTitledBorder(new LineBorder(CIAN_ELECTRICO), " MONITOR DE COLA (DETECCIÓN DE NECESIDAD) "));

        // PANEL DE DESPACHO MANUAL
        JPanel pnlDespacho = new JPanel(new GridLayout(1, 2, 15, 15));
        pnlDespacho.setOpaque(false);

        JPanel pnlControlSalidas = new JPanel(new GridLayout(6, 1, 8, 8));
        pnlControlSalidas.setBackground(GRIS_PANEL);
        pnlControlSalidas.setBorder(new EmptyBorder(10,10,10,10));

        cbLocalidades = new JComboBox<>(new String[]{"USAQUÉN", "CHAPINERO", "KENNEDY", "SUBA", "ENGATIVÁ", "BOSA", "TEUSAQUILLO"});
        cbTipoUnidad = new JComboBox<>(new String[]{"AMBULANCIA S.M.U.", "CUERPO DE BOMBEROS"});
        JButton btnDespachar = new JButton("EJECUTAR DESPACHO");
        btnDespachar.setBackground(AMARILLO_BOGOTA);
        btnDespachar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblLoc = new JLabel("LOCALIDAD DE SALIDA:");
        lblLoc.setForeground(Color.WHITE);
        JLabel lblUni = new JLabel("UNIDAD A ENVIAR:");
        lblUni.setForeground(Color.WHITE);

        pnlControlSalidas.add(lblLoc);
        pnlControlSalidas.add(cbLocalidades);
        pnlControlSalidas.add(lblUni);
        pnlControlSalidas.add(cbTipoUnidad);
        pnlControlSalidas.add(btnDespachar);

        txtPlanos = new JTextArea();
        txtPlanos.setBackground(new Color(20, 15, 10));
        txtPlanos.setForeground(Color.ORANGE);
        txtPlanos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtPlanos.setText("\n  [ PLANOS TÉCNICOS BOGOTÁ ]\n  LOCALIDADES: MONITOREADAS\n  ESTADO: LISTO PARA OPERAR");

        pnlDespacho.add(pnlControlSalidas);
        pnlDespacho.add(new JScrollPane(txtPlanos));

        pnlCentro.add(scrollRadar);
        pnlCentro.add(pnlDespacho);
        add(pnlCentro, BorderLayout.CENTER);

        // --- 4. PANEL DERECHO: LOG DE SISTEMA ---
        txtLogGeneral = new JTextArea();
        txtLogGeneral.setBackground(GRIS_PANEL);
        txtLogGeneral.setForeground(CIAN_ELECTRICO);
        txtLogGeneral.setEditable(false);
        txtLogGeneral.setPreferredSize(new Dimension(280, 0));
        JScrollPane scrollLog = new JScrollPane(txtLogGeneral);
        scrollLog.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.DARK_GRAY), " LOG DE ACTIVIDAD "));
        add(scrollLog, BorderLayout.EAST);

        // --- 5. FOOTER: ESTADO Y PILA ---
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(Color.BLACK);
        pnlFooter.setPreferredSize(new Dimension(100, 45));
        lblStatus = new JLabel("  SISTEMA DE ANÁLISIS DE RIESGO - BOGOTÁ D.C. ACTIVO");
        lblStatus.setForeground(Color.WHITE);
        JButton btnUndo = new JButton("⟲ CANCELAR ÚLTIMA SALIDA (PILA)");
        btnUndo.setBackground(new Color(60, 60, 60));
        btnUndo.setForeground(Color.WHITE);
        pnlFooter.add(lblStatus, BorderLayout.WEST);
        pnlFooter.add(btnUndo, BorderLayout.EAST);
        add(pnlFooter, BorderLayout.SOUTH);

        // --- LÓGICA DE BOTONES ---

        btnAdd.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtCodMedicina.getText());
                String nombre = JOptionPane.showInputDialog(this, "Nombre del Medicamento:");
                if(nombre != null && !nombre.isEmpty()){
                    stockBST.insertar(id, nombre);
                    txtInventarioRef.append(" > REF-" + id + ": " + nombre + "\n");
                    txtCodMedicina.setText("");
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "ID numérico requerido."); }
        });

        btnSearch.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtCodMedicina.getText());
                txtLogGeneral.setText(stockBST.buscarConPasos(stockBST.raiz, id, "RASTREO BST EN BODEGA:\n"));
            } catch (Exception ex) { txtLogGeneral.setText("Error en búsqueda."); }
        });

        btnDespachar.addActionListener(e -> {
            String unidad = (String) cbTipoUnidad.getSelectedItem();
            String loc = (String) cbLocalidades.getSelectedItem();
            String emergencia = colaEmergencias.atenderLlamada();
            
            if (!emergencia.equals("Sin llamadas")) {
                historialPila.registrarDespacho(unidad + " [" + loc + "] -> " + emergencia);
                txtLogGeneral.append("!!! DESPACHO: " + unidad + " hacia " + emergencia + "\n");
                txtRadar.setText(colaEmergencias.mostrarCola());
                txtPlanos.setText("\n  PLANOS ACTUALIZADOS:\n  UNIDAD EN RUTA: " + unidad + "\n  LOCALIDAD ORIGEN: " + loc + "\n  DESTINO: " + emergencia);
            } else {
                JOptionPane.showMessageDialog(this, "No hay emergencias en la cola.");
            }
        });

        btnUndo.addActionListener(e -> {
            String log = historialPila.deshacerUltimo();
            lblStatus.setText("  ULTIMA ACCIÓN: " + log);
            txtLogGeneral.append("[PILA]: " + log + "\n");
        });

        // --- TIMER: GENERADOR DE LLAMADAS POR TIPO ---
        String[] barrios = {"Usaquén", "Kennedy", "Suba", "Engativá", "Bosa", "Fontibón", "Chapinero"};
        Timer t = new Timer(8000, e -> {
            String barrio = barrios[(int)(Math.random() * barrios.length)];
            boolean esIncendio = Math.random() > 0.5;
            String tipo = esIncendio ? "🔥 [BOMBEROS]" : "🚑 [AMBULANCIA]";
            
            colaEmergencias.recibirLlamada(tipo + " en " + barrio);
            txtRadar.setText(colaEmergencias.mostrarCola());
        });
        t.start();
    }

    private void cargarMedicamentosIniciales() {
        // IDs estratégicos para que el árbol esté balanceado
        Object[][] meds = {
            {500, "Adrenalina"}, {250, "Morfina"}, {750, "Salbutamol"},
            {100, "Atropina"}, {350, "Dopamina"}, {600, "Insulina"}, {900, "Diazepam"}
        };
        
        txtInventarioRef.setText(" [ CÓDIGOS DE REFERENCIA ]\n -------------------------\n");
        for (Object[] m : meds) {
            stockBST.insertar((int)m[0], (String)m[1]);
            txtInventarioRef.append(" > REF-" + m[0] + ": " + m[1] + "\n");
        }
    }

    public static void main(String[] args) {
        // Estilo visual del sistema operativo
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            SmartCityGUI gui = new SmartCityGUI();
            gui.setVisible(true);
            gui.setLocationRelativeTo(null);
        });
    }
}