package org.freaknowledge;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.freaknowledge.trip.TripProcess;
import org.jbpm.process.instance.impl.demo.DoNothingWorkItemHandler;
import org.junit.Assert;

public class CreateProcessInstanceTest {

    private StatefulKnowledgeSession ksession;

    @org.junit.Before
    public void createStatefulKnowledgeSession() {

        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(new ClassPathResource("org/freaknowledge/Rule.drl"), ResourceType.DRL);
        knowledgeBuilder.add(new ClassPathResource("org/freaknowledge/TripProcess.bpmn"), ResourceType.BPMN2);

        ksession = knowledgeBuilder.newKnowledgeBase().newStatefulKnowledgeSession();
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", new DoNothingWorkItemHandler());
    }

    @org.junit.Test
    public void happyFlow() {

        TripProcess process = new TripProcess();
        process.setMessage("Hello World!");
        process.setImportant(true);

        ksession.insert(process);
        ksession.fireAllRules();

        Assert.assertNotNull(process.getFactHandle());
        Assert.assertNotNull(process.getProcessInstanceId());

        ksession.startProcessInstance(process.getProcessInstanceId());
    }

    @org.junit.Test
    public void failure() {

        TripProcess process = new TripProcess();
        process.setImportant(true);

        ksession.insert(process);
        ksession.fireAllRules();

        Assert.assertNotNull(process.getFactHandle());
        Assert.assertNull(process.getProcessInstanceId());
    }
}
