package org.freaknowledge;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;

public class CreateProcessInstanceTest {

    private KnowledgeBase newKnowledgeBase;

    @org.junit.Before
    public void createKnowledgeBase() {

        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(new ClassPathResource("org/freaknowledge/Rule.drl"), ResourceType.DRL);
        knowledgeBuilder.add(new ClassPathResource("org/freaknowledge/TestProcess.rf"), ResourceType.DRF);

        newKnowledgeBase = knowledgeBuilder.newKnowledgeBase();
    }

    @org.junit.Test
    public void happyFlow() {

        StatefulKnowledgeSession knowledgeSession = newKnowledgeBase.newStatefulKnowledgeSession();

        TestProcess process = new TestProcess();
        process.setMessage("Hello World!");
        process.setImportant(true);
        process.setFactHandle(knowledgeSession.insert(process).toExternalForm());

        knowledgeSession.fireAllRules();
        
        
    }
}
