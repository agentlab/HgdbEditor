package ru.agentlab.wordnet.editor.ui.parts;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.fx.core.di.Service;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;
import net.sf.extjwnl.service.IExtjwnlService;
import net.sf.extjwnl.utilities.Examples;

public class DictionaryPart {

    @Inject
    @Service
    private IExtjwnlService extjwnlService;

    private static List<Dictionary> dictionary;

    public DictionaryPart() {
        System.out.println("Hhhh"); //$NON-NLS-1$
    }

    TreeView<String> treeView;

    @PostConstruct
    void initUI(BorderPane pane) {
        try
        {
            this.dictionary = this.extjwnlService.getDictionary();

            TreeItem<String> root = new TreeItem<>("VERB");
            root.setExpanded(true);
            treeView = new TreeView<>(root);

            EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
                handleMouseClicked(event);
            };

            treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);

            try
            {
                Iterator<IndexWord> iter = dictionary.get(0).getIndexWordIterator(POS.VERB);
                while (iter.hasNext())
                {
                    String str = iter.next().getLemma();
                    root.getChildren().add(new TreeItem<>(str));
                }
            }
            catch (JWNLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            pane.setCenter(treeView);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void handleMouseClicked(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();

        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell)node).getText() != null))
        {
            String name = (String)((TreeItem)treeView.getSelectionModel().getSelectedItem()).getValue();

            try
            {
                IndexWord word = dictionary.get(0).getIndexWord(POS.VERB, name);
                PointerTargetNodeList hyponyms = PointerUtils.getDirectHyponyms(word.getSenses().get(0));
                PointerTargetNodeList hypernyms = PointerUtils.getDirectHypernyms(word.getSenses().get(0));
                PointerTargetNodeList synonyms = PointerUtils.getSynonyms(word.getSenses().get(0));
                PointerTargetNodeList antonyms = PointerUtils.getAntonyms(word.getSenses().get(0));
                PointerTargetNodeList holonyms = PointerUtils.getHolonyms(word.getSenses().get(0));
                PointerTargetNodeList CoordinateTerms = PointerUtils.getCoordinateTerms(word.getSenses().get(0));
//                WordPart wordsChange = WordPart.getWp();
//                wordsChange.initializeTree(name, hypernyms, hyponyms, synonyms, antonyms, holonyms, CoordinateTerms);
            }
            catch (JWNLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public synchronized void setDictionary(IExtjwnlService service) {
        System.err.println("Service was set. Thank you DS!");

        this.extjwnlService = service;
        this.dictionary = this.extjwnlService.getDictionary();

        if (null != dictionary)
        {
            try
            {
                new Examples(this.dictionary.get(0)).go();
            }
            catch (JWNLException | CloneNotSupportedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // Method will be used by DS to unset the quote service
    public synchronized void unsetDictionary(IExtjwnlService service) {
        if (this.dictionary != null)
        {
            this.dictionary = null;
        }
        if (this.extjwnlService == service)
        {
            this.extjwnlService = null;
        }
    }
}
