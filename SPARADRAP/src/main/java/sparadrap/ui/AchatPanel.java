
package sparadrap.ui;

import sparadrap.Achat;
import sparadrap.Clients;
import sparadrap.Medecins;
import sparadrap.Medicaments;
import sparadrap.Mutuelle;
import sparadrap.Pharmacie;


import javax.swing.*;
import java.awt.*;

public class AchatPanel extends JPanel {
    private final Pharmacie service;
    private final JComboBox<Clients> cbClient;
    private final JComboBox<Medecins> cbMedecin;
    private final JCheckBox cbAvecMutuelle;
    private final JComboBox<Mutuelle> cbMutuelle;
    private final JComboBox<Medicaments> cbMedicament;
    private final JSpinner spQty;

    public AchatPanel(Pharmacie s){
        this.service = s;
        setLayout(new BorderLayout());
        add(new JLabel("Saisir un achat"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        cbClient = new JComboBox<>(service.getClients().toArray(new Clients[0]));
        cbMedecin = new JComboBox<>(service.getMedecins().toArray(new Medecins[0]));
        cbAvecMutuelle = new JCheckBox("Avec mutuelle");
        cbMutuelle = new JComboBox<>(service.getMutuelles().toArray(new Mutuelle[0]));
        cbMutuelle.setEnabled(false);
        // Activer ou désactiver la sélection de mutuelle
        cbAvecMutuelle.addActionListener(e -> cbMutuelle.setEnabled(cbAvecMutuelle.isSelected()));
        // Ajuste automatiquement la mutuelle selon client sélectionné
        cbClient.addActionListener(e -> onClientChange());
        cbMedicament = new JComboBox<>(service.getMedicaments().toArray(new Medicaments[0]));
        spQty = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));

        int y=0;
        c.gridx=0;
        c.gridy=y;
        form.add(new JLabel("Client"), c);
        c.gridx=1;
        form.add(cbClient, c); y++;
        c.gridx=0;
        c.gridy=y;
        form.add(new JLabel("Médecin"), c);
        c.gridx=1;
        form.add(cbMedecin, c); y++;
        c.gridx=0;
        c.gridy=y;
        form.add(cbAvecMutuelle, c);
        c.gridx=1;
        form.add(cbMutuelle, c); y++;
        c.gridx=0;
        c.gridy=y;
        form.add(new JLabel("Médicament"), c);
        c.gridx=1;
        form.add(cbMedicament, c); y++;
        c.gridx=0;
        c.gridy=y;
        form.add(new JLabel("Quantité"), c);
        c.gridx=1;
        form.add(spQty, c); y++;

        add(form, BorderLayout.CENTER);

        JButton valider = new JButton("Valider achat");
        // Valider achat pour appliqur les règles métier
        valider.addActionListener(e -> onValider());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(valider);
        add(south, BorderLayout.SOUTH);
    }

    private void onValider(){
        Clients client = (Clients) cbClient.getSelectedItem();
        Medecins medecins = (Medecins) cbMedecin.getSelectedItem();
        Mutuelle mutuelle = cbAvecMutuelle.isSelected() ? (Mutuelle) cbMutuelle.getSelectedItem() : null;
        Medicaments medicaments = (Medicaments) cbMedicament.getSelectedItem();
        int qty = (Integer) spQty.getValue();

        // Pour le calcul etvalidation de l'achat
        Achat achat = service.acheter(client, medecins, mutuelle, medicaments, qty);
        if (achat == null){
            JOptionPane.showMessageDialog(this, "Achat impossible (stock insuffisant ?)", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Achat enregistré. Total: " + achat.getTotal() + "€\nStock restant: " + medicaments.getQuantite());
    }

    private void onClientChange(){
        Clients client = (Clients) cbClient.getSelectedItem();
        Mutuelle m = client != null ? client.getMutuelle() : null;
        if (m != null){
            // Si le client a une mutuelle, on la sélectionne et on verrouille le choix
            cbAvecMutuelle.setSelected(true);
            cbMutuelle.setEnabled(false);
            cbMutuelle.setSelectedItem(m);
        } else {
            cbAvecMutuelle.setSelected(false);
            cbMutuelle.setEnabled(false);
        }
    }
}
