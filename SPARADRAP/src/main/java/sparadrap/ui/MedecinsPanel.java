package sparadrap.ui;

import sparadrap.Medecins;
import sparadrap.Pharmacie;
import sparadrap.controller.MedecinsController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinsPanel extends JPanel {

    private final Pharmacie service;
    private final MedecinsController controller;
    private final JTable table;
    private List<Medecins> currentMedecins = new ArrayList<>();

    public MedecinsPanel(Pharmacie service) {
        this.service = service;
        this.controller = new MedecinsController();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Gestion des médecins");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Renommer");
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Rafraîchir");

        actions.add(addButton);
        actions.add(editButton);
        actions.add(deleteButton);
        actions.add(refreshButton);
        add(actions, BorderLayout.SOUTH);

        addButton.addActionListener(e -> onAdd());
        editButton.addActionListener(e -> onEdit());
        deleteButton.addActionListener(e -> onDelete());
        refreshButton.addActionListener(e -> loadMedecins());

        loadMedecins();
    }

    private void loadMedecins() {
        currentMedecins = controller.listMedecins();

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Id", "Nom"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Medecins m : currentMedecins) {
            model.addRow(new Object[]{m.getId(), m.getNom()});
        }

        table.setModel(model);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(40);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
        }
    }

    private Medecins getSelectedMedecin() {
        int row = table.getSelectedRow();
        if (row < 0 || row >= currentMedecins.size()) {
            return null;
        }
        return currentMedecins.get(row);
    }

    private void onAdd() {
        String nom = JOptionPane.showInputDialog(
                this,
                "Nom du médecin :",
                "Nouveau médecin",
                JOptionPane.PLAIN_MESSAGE
        );

        if (nom == null) {
            return;
        }

        nom = nom.trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Le nom ne peut pas être vide.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            controller.createMedecin(nom);
            loadMedecins();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de la création du médecin : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onEdit() {
        Medecins selected = getSelectedMedecin();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionne un médecin dans la liste.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String nouveauNom = (String) JOptionPane.showInputDialog(
                this,
                "Nouveau nom :",
                "Modifier le médecin",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                selected.getNom()
        );

        if (nouveauNom == null) {
            return;
        }

        nouveauNom = nouveauNom.trim();
        if (nouveauNom.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Le nom ne peut pas être vide.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            controller.updateMedecin(selected, nouveauNom);
            loadMedecins();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de la mise à jour du médecin : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onDelete() {
        Medecins selected = getSelectedMedecin();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionne un médecin dans la liste.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Supprimer le médecin \"" + selected.getNom() + "\" ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controller.deleteMedecin(selected);
            loadMedecins();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de la suppression du médecin : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
