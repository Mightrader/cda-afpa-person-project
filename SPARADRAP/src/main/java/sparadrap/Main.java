package sparadrap;

import sparadrap.ui.AchatPanel;
import sparadrap.ui.MedecinsPanel;
import sparadrap.ui.ClientsPanel;
import sparadrap.ui.MedicamentsPanel;
import sparadrap.ui.MutuellesPanel;

import sparadrap.db.Database;
import sparadrap.db.AchatRepository;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = LoggerConfig.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        LOG.info("===== Démarrage de SPARADRAP =====");

        Database.init();
        AchatRepository repo = new AchatRepository();
        repo.createSchema();
        repo.seed();
        try {
            repo.savePurchase("Dupont Jean", "Dr Martin Alice", "Axa Santé", "Doliprane", 1);
            LOG.info("Insertion d'un achat de test OK.");
        } catch (Exception e) {
            LOG.warning("Insertion de test KO : " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            sparadrap.Pharmacie service = new sparadrap.Pharmacie();

            JFrame frame = new JFrame("SPARADRAP – Gestion de pharmacie");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 650);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());

            JLabel statusLabel = new JLabel("Prêt.");
            JPanel statusBar = new JPanel(new BorderLayout());
            statusBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            statusBar.add(statusLabel, BorderLayout.WEST);
            frame.add(statusBar, BorderLayout.SOUTH);

            JPanel header = new JPanel(new BorderLayout());
            header.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            header.setBackground(new Color(34, 40, 49));

            JLabel titleLabel = new JLabel("Sparadrap");
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));


            JPanel titlePanel = new JPanel();
            titlePanel.setOpaque(false);
            titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
            titlePanel.add(titleLabel);
            header.add(titlePanel, BorderLayout.WEST);

            JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            navPanel.setOpaque(false);

            ButtonGroup navGroup = new ButtonGroup();

            JToggleButton bHome = createNavButton("Accueil");
            JToggleButton bAchat = createNavButton("Achats");
            JToggleButton bMed = createNavButton("Médecins");
            JToggleButton bCli = createNavButton("Clients");
            JToggleButton bMedics = createNavButton("Médicaments");
            JToggleButton bMut = createNavButton("Mutuelles");

            navGroup.add(bHome);
            navGroup.add(bAchat);
            navGroup.add(bMed);
            navGroup.add(bCli);
            navGroup.add(bMedics);
            navGroup.add(bMut);

            navPanel.add(bHome);
            navPanel.add(bAchat);
            navPanel.add(bMed);
            navPanel.add(bCli);
            navPanel.add(bMedics);
            navPanel.add(bMut);

            header.add(navPanel, BorderLayout.CENTER);

            JLabel clockLabel = new JLabel();
            clockLabel.setForeground(new Color(200, 200, 200));
            clockLabel.setFont(clockLabel.getFont().deriveFont(11f));
            updateClock(clockLabel);
            javax.swing.Timer timer = new javax.swing.Timer(60_000, e -> updateClock(clockLabel));
            timer.setRepeats(true);
            timer.start();
            header.add(clockLabel, BorderLayout.EAST);

            frame.add(header, BorderLayout.NORTH);

            JPanel cards = new JPanel(new CardLayout());
            frame.add(cards, BorderLayout.CENTER);

            JPanel homePanel = new JPanel(new BorderLayout());
            JLabel homeLabel = new JLabel("<html><h2 style='margin:8px'>Bienvenue dans SPARADRAP</h2>",
                    SwingConstants.CENTER);
            homePanel.add(homeLabel, BorderLayout.CENTER);

            AchatPanel achatsPanel = new AchatPanel(service);
            MedecinsPanel medPanel = new MedecinsPanel(service);
            ClientsPanel cliPanel = new ClientsPanel(service);
            MedicamentsPanel medsPanel = new MedicamentsPanel(service);
            MutuellesPanel mutPanel = new MutuellesPanel(service);

            cards.add(homePanel, "HOME");
            cards.add(achatsPanel, "ACHATS");
            cards.add(medPanel, "MEDECINS");
            cards.add(cliPanel, "CLIENTS");
            cards.add(medsPanel, "MEDICAMENTS");
            cards.add(mutPanel, "MUTUELLES");

            CardLayout cardLayout = (CardLayout) cards.getLayout();

            bHome.addActionListener(e -> {
                cardLayout.show(cards, "HOME");
                statusLabel.setText("Prêt.");
                LOG.info("Onglet affiché : Accueil");
            });

            bAchat.addActionListener(e -> {
                cardLayout.show(cards, "ACHATS");
                statusLabel.setText("Onglet : Achats");
                LOG.info("Onglet affiché : Achats");
            });

            bMed.addActionListener(e -> {
                cardLayout.show(cards, "MEDECINS");
                statusLabel.setText("Onglet : Médecins");
                LOG.info("Onglet affiché : Médecins");
            });

            bCli.addActionListener(e -> {
                cardLayout.show(cards, "CLIENTS");
                statusLabel.setText("Onglet : Clients");
                LOG.info("Onglet affiché : Clients");
            });

            bMedics.addActionListener(e -> {
                cardLayout.show(cards, "MEDICAMENTS");
                statusLabel.setText("Onglet : Médicaments");
                LOG.info("Onglet affiché : Médicaments");
            });

            bMut.addActionListener(e -> {
                cardLayout.show(cards, "MUTUELLES");
                statusLabel.setText("Onglet : Mutuelles");
                LOG.info("Onglet affiché : Mutuelles");
            });

            bHome.setSelected(true);
            cardLayout.show(cards, "HOME");

            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    LOG.info("Fermeture de l'application.");
                }
            });

            frame.setVisible(true);
            LOG.info("Fenêtre initialisée et visible.");
        });
    }

    private static JToggleButton createNavButton(String text) {
        JToggleButton button = new JToggleButton(text);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));

        button.addChangeListener(e -> {
            if (button.isSelected()) {
                button.setOpaque(true);
                button.setBackground(new Color(0, 173, 181));
            } else {
                button.setOpaque(false);
                button.setBackground(null);
            }
        });

        return button;
    }

    private static void updateClock(JLabel label) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        label.setText(LocalDateTime.now().format(formatter));
    }
}
