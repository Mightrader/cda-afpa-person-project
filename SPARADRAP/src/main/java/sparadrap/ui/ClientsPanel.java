package sparadrap.ui;

import sparadrap.Clients;
import sparadrap.Pharmacie;
import sparadrap.controller.ClientsController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel Swing de gestion des clients.
 * <p>
 * Affiche la liste des clients et permet d'ajouter, renommer,
 * supprimer et rafraîchir les données via {@link ClientsController}.
 */
public class ClientsPanel extends JPanel {

    private final Pharmacie service;
    private final ClientsController controller;
    private final JTable table;
    private List<Clients> currentClients = new ArrayList<>();

    public ClientsPanel(Pharmacie service) {
        this.service = service;
        this.controller = new ClientsController();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Gestion des clients");
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
        refreshButton.addActionListener(e -> loadClients());

        loadClients();
    }

    /**
     * Recharge les clients depuis le contrôleur et met à jour la table.
     */
    private void loadClients() {
        currentClients = controller.listClients();

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Id", "Nom"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Clients c : currentClients) {
            model.addRow(new Object[]{c.getId(), c.getNom()});
        }

        table.setModel(model);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(40);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
        }
    }

    private Clients getSelectedClient() {
        int row = table.getSelectedRow();
        if (row < 0 || row >= currentClients.size()) {
            return null;
        }
        return currentClients.get(row);
    }

    private void onAdd() {
        String nom = JOptionPane.showInputDialog(
                this,
                "Nom du client :",
                "Nouveau client",
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
            controller.createClient(nom);
            loadClients();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de la création du client : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onEdit() {
        Clients selected = getSelectedClient();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionne un client dans la liste.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String nouveauNom = (String) JOptionPane.showInputDialog(
                this,
                "Nouveau nom :",
                "Modifier le client",
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
            controller.updateClient(selected, nouveauNom);
            loadClients();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de la mise à jour du client : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onDelete() {
        Clients selected = getSelectedClient();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionne un client dans la liste.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Supprimer le client \"" + selected.getNom() + "\" ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controller.deleteClient(selected);
            loadClients();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de la suppression du client : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
