 

package sparadrap.ui;

import sparadrap.Medicaments;
import sparadrap.Pharmacie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MedicamentsPanel extends JPanel {
    private final Pharmacie service;
    private final JTable table;
    public MedicamentsPanel(Pharmacie s){ this.service=s;
        setLayout(new BorderLayout());
        add(new JLabel("Liste des médicaments"), BorderLayout.NORTH);
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        JButton refresh = new JButton("Rafraîchir");
        // Recharge les données à partirde la couche métier
        refresh.addActionListener(e->load());
        add(refresh, BorderLayout.SOUTH);
        load();
    }
    private void load(){ List<Medicaments> l = service.getMedicaments();
        DefaultTableModel m = new DefaultTableModel(new Object[]{"Nom","Catégorie","Prix","Quantité"},0){
            @Override public boolean isCellEditable(int row, int column){ return false; }
        };
        for (Medicaments d: l) m.addRow(new Object[]{d.getNom(), d.getCategorie(), d.getPrix(), d.getQuantite()});
        table.setModel(m);
    }
}
