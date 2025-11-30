package cookbook;

import java.util.List;

import java.awt.*;
import javax.swing.*;

import cookbook.gui.WrapLayout;
import cookbook.recipe.Ingredient;
import cookbook.recipe.Recipe;

public class CookbookGUI extends JFrame {
    private static final Color BACKGROUND_COLOR = new Color(0x2b2a33);
    private static final Color PRIMARY_COLOR = new Color(0x42414d);
    private static final Color TEXT_COLOR = Color.WHITE;

    private List<Recipe> recipes;

    CookbookGUI() {
        super("Cookbook");
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(BACKGROUND_COLOR);

        renderStartPage();

        this.setVisible(true);
    }

    private void renderStartPage() {
        JPanel root = (JPanel) this.getContentPane();
        root.setLayout(new GridBagLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Cookbook");
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel tip = new JLabel("Tell me your ingredients to start!");
        tip.setForeground(TEXT_COLOR);
        tip.setFont(new Font("SansSerif", Font.PLAIN, 20));
        tip.setAlignmentX(Component.CENTER_ALIGNMENT);
        tip.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        JTextField input = new JTextField();
        input.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        input.setMaximumSize(new Dimension(600, 50));
        input.setPreferredSize(new Dimension(600, 50));
        input.setForeground(TEXT_COLOR);
        input.setCaretColor(TEXT_COLOR);
        input.setBackground(PRIMARY_COLOR);
        input.setFont(new Font("SansSerif", Font.PLAIN, 20));
        input.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton submit = new JButton("Get recipes");
        submit.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.setFont(new Font("SansSerif", Font.PLAIN, 16));
        submit.setForeground(TEXT_COLOR);
        submit.setBackground(PRIMARY_COLOR);

        submit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                submit.setBackground(PRIMARY_COLOR.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                submit.setBackground(PRIMARY_COLOR);
            }
        });

        submit.addActionListener(e -> {
            if (input.getText().length() == 0)
                return;
            submit.setEnabled(false);
            recipes = Parser.getRecipes(input.getText());
            renderRecipeSelection();
        });

        content.add(title);
        content.add(tip);
        content.add(input);
        content.add(Box.createVerticalStrut(15));
        content.add(submit);

        root.add(content, new GridBagConstraints());
    }

    private void renderRecipeSelection() {
        getContentPane().removeAll();
        getContentPane().setBackground(BACKGROUND_COLOR);
        getContentPane().setLayout(new GridBagLayout());

        JPanel root = new JPanel();
        root.setOpaque(false);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Choose a Recipe");
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        root.add(title);

        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new GridLayout(1, 3, 30, 0));
        container.setMaximumSize(new Dimension(700, 250));

        for (Recipe r : recipes) {
            JButton btn = new JButton("<html><center>"
                    + r.getName() + "<br><br>"
                    + r.getDuration() + " min"
                    + "</center></html>");

            btn.setPreferredSize(new Dimension(180, 180));
            btn.setBackground(PRIMARY_COLOR);
            btn.setForeground(TEXT_COLOR);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
            btn.setFocusPainted(false);
            btn.setBorder(null);

            Color normal = PRIMARY_COLOR;
            Color hover = PRIMARY_COLOR.brighter();

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btn.setBackground(hover);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btn.setBackground(normal);
                }
            });

            btn.addActionListener(e -> renderRecipeSteps(r));

            container.add(btn);
        }

        root.add(container);

        getContentPane().add(root, new GridBagConstraints());
        revalidate();
        repaint();
    }

    private void renderRecipeSteps(Recipe recipe) {

        getContentPane().removeAll();
        getContentPane().setBackground(BACKGROUND_COLOR);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel rootPanel = new JPanel();
        rootPanel.setOpaque(false);
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(rootPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        JLabel title = new JLabel(recipe.getName());
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        rootPanel.add(title);

        JLabel ingredientsLabel = new JLabel("Ingredients");
        ingredientsLabel.setForeground(TEXT_COLOR);
        ingredientsLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        ingredientsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ingredientsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        rootPanel.add(ingredientsLabel);

        JPanel ingredientsPanel = new JPanel(new WrapLayout(WrapLayout.CENTER, 5, 5));
        ingredientsPanel.setOpaque(false);

        for (Ingredient ingredient : recipe.getIngredients()) {
            String capitalizedName = ingredient.getName().substring(0,1).toUpperCase() + ingredient.getName().substring(1).toLowerCase();
            JLabel ingredientLabel = new JLabel(capitalizedName
                                                + " - " + ingredient.getQuantity() + " " + ingredient.getUnit()
                                                + (ingredient.isAvailable() ? " (Available)" : ""));
            ingredientLabel.setOpaque(true);
            ingredientLabel.setForeground(TEXT_COLOR);
            ingredientLabel.setBackground(PRIMARY_COLOR);
            ingredientLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            ingredientLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            ingredientsPanel.add(ingredientLabel);
        }
        ingredientsPanel.setMaximumSize(new Dimension((int)(getWidth()*0.8), ingredientsPanel.getPreferredSize().height));
        rootPanel.add(ingredientsPanel);

        JLabel stepsLabel = new JLabel("Steps");
        stepsLabel.setForeground(TEXT_COLOR);
        stepsLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        stepsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        stepsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rootPanel.add(stepsLabel);

        StringBuilder steps = new StringBuilder();
        int index = 1;
        for (String step : recipe.getSteps()) {
            steps.append(index++).append(". ").append(step)
            .append("\n");
        }

        JTextArea stepsArea = new JTextArea(steps.toString());
        stepsArea.setLineWrap(true);
        stepsArea.setEditable(false);
        stepsArea.setMaximumSize(new Dimension((int)(getWidth()*0.8), stepsArea.getPreferredSize().height));
        stepsArea.setOpaque(false);
        stepsArea.setForeground(TEXT_COLOR);
        stepsArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        stepsArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        stepsArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        rootPanel.add(stepsArea);

        JButton backButton = new JButton("← Go back");
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setForeground(TEXT_COLOR);
        backButton.setBackground(PRIMARY_COLOR);

        backButton.addActionListener(e -> {
            renderRecipeSelection();
        });

        rootPanel.add(backButton);

        rootPanel.add(Box.createVerticalStrut(15));
        rootPanel.add(Box.createVerticalGlue());

        getContentPane().add(scrollPane);

        revalidate();
        repaint();
    }

}
