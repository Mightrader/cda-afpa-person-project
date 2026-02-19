package sparadrap.ui;

import sparadrap.Mutuelle;
import sparadrap.Pharmacie;
import sparadrap.controller.MutuellesController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MutuellesPanel extends JPanel {

    private final Pharmacie service;
    private final MutuellesController controller;
    private final JTable table;
    private List<Mutuelle> currentMutuelles = new ArrayList<>();

    public MutuellesPanel(Pharmacie service) {
        this.service = service;
        this.controller = new MutuellesController(service);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Gestion des mutuelles");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
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
        refreshButton.addActionListener(e -> loadMutuelles());

        loadMutuelles();
    }

    private void loadMutuelles() {
        currentMutuelles = controller.listMutuelles();

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Nom", "Taux", "Ville", "Téléphone"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Mutuelle m : currentMutuelles) {
            model.addRow(new Object[]{
                    m.getNom(),
                    m.getTauxPriseEnCharge(),
                    m.getVille(),
                    m.getTelephone()
            });
        }

        table.setModel(model);
    }

    private Mutuelle getSelectedMutuelle() {
        int row = table.getSelectedRow();
        if (row < 0 || row >= currentMutuelles.size()) {
            return null;
        }
        return currentMutuelles.get(row);
    }

    private void onAdd() {
        MutuelleFormData data = askMutuelleData("Nouvelle mutuelle", null);
        if (data == null) {
            return;
        }
        controller.createMutuelle(data.nom, data.telephone, data.email, data.ville, data.taux);
        loadMutuelles();
    }

    private void onEdit() {
        Mutuelle selected = getSelectedMutuelle();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionne une mutuelle dans la liste.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        MutuelleFormData data = askMutuelleData("Modifier la mutuelle", selected);
        if (data == null) {
            return;
        }

        controller.updateMutuelle(selected, data.nom, data.telephone, data.email, data.ville, data.taux);
        loadMutuelles();
    }

    private void onDelete() {
        Mutuelle selected = getSelectedMutuelle();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionne une mutuelle dans la liste.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Supprimer la mutuelle \"" + selected.getNom() + "\" ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        controller.deleteMutuelle(selected);
        loadMutuelles();
    }

    // ---- Saisie des données de mutuelle ----

    private static class MutuelleFormData {
        String nom;
        String telephone;
        String email;
        String ville;
        double taux;
    }

    private MutuelleFormData askMutuelleData(String title, Mutuelle base) {
        JTextField nomField = new JTextField(base != null ? base.getNom() : "");
        JTextField telField = new JTextField(base != null ? base.getTelephone() : "");
        JTextField emailField = new JTextField(base != null ? base.getEmail() : "");
        JTextField villeField = new JTextField(base != null ? base.getVille() : "");
        JTextField tauxField = new JTextField(
                base != null ? String.valueOf(base.getTauxPriseEnCharge()) : ""
        );

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Nom :"));
        form.add(nomField);
        form.add(new JLabel("Téléphone :"));
        form.add(telField);
        form.add(new JLabel("Email :"));
        form.add(emailField);
        form.add(new JLabel("Ville :"));
        form.add(villeField);
        form.add(new JLabel("Taux (0.0 à 1.0) :"));
        form.add(tauxField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        String nom = nomField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Le nom est obligatoire.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        double taux = 0.0;
        String tauxText = tauxField.getText().trim();
        if (!tauxText.isEmpty()) {
            try {
                taux = Double.parseDouble(tauxText.replace(',', '.'));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Le taux doit être un nombre.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
                return null;
            }
            if (taux < 0.0 || taux > 1.0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Le taux doit être compris entre 0 et 1.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
                return null;
            }
        }

        MutuelleFormData data = new MutuelleFormData();
        data.nom = nom;
        data.telephone = telField.getText().trim();
        data.email = emailField.getText().trim();
        data.ville = villeField.getText().trim();
        data.taux = taux;

        return data;
    }
}
