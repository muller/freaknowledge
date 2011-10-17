package org.freaknowledge.protest

import java.lang.reflect.Method

import org.drools.KnowledgeBase
import org.drools.KnowledgeBaseFactory
import org.drools.builder.KnowledgeBuilder
import org.drools.builder.KnowledgeBuilderFactory
import org.drools.builder.ResourceType
import org.drools.definitions.impl.KnowledgePackageImp
import org.drools.io.ResourceFactory
import org.drools.rule.Package
import org.drools.runtime.StatefulKnowledgeSession
import org.drools.runtime.process.ProcessContext
import org.drools.runtime.rule.FactHandle
import org.jbpm.process.instance.impl.Action
import org.jbpm.process.instance.impl.demo.DoNothingWorkItemHandler
import org.jbpm.ruleflow.core.RuleFlowProcessFactory


class Test {

	When when

	Closure then

	List<String> resources = []

	Map<String, Closure> humanTasks = [:]

	Map<String, Closure> subProcesses = [:]
}

class When {

	List actions = []

	Closure closure

	String description
}

class SubSystem {

	String id

	Closure action

	RuleFlowProcessFactory factory

	def create() {

		factory = RuleFlowProcessFactory.createProcess(id)
		factory.name(id).packageName('mock').startNode(0).done().endNode(2).done()
		factory.actionNode(1).action(new Action() {

					void execute(ProcessContext context) {

						action.call(context)
					}
				}).done()

		factory.connection(0, 1)
		factory.connection(1, 2)

		return factory.process
	}
}

class TestRuntime {

	Test test

	Map<String, FactHandle> factHandles = [:]

	StatefulKnowledgeSession session

	def createSubProcessMockPackages() {

		Package mockPackage = new Package('mock')

		test.subProcesses.each { processId, closure ->

			SubSystem mock = new SubSystem(id: processId, action: closure)

			mockPackage.addProcess mock.create()
		}

		return [
			new KnowledgePackageImp(mockPackage)
		]
	}

	def loadResourcesPackages() {

		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder()

		test.resources.findAll { it.endsWith '.drl' } each {

			knowledgeBuilder.add(ResourceFactory.newClassPathResource(it), ResourceType.DRL);
		}

		test.resources.findAll { it.endsWith '.rf' } each {

			knowledgeBuilder.add(ResourceFactory.newClassPathResource(it), ResourceType.DRF);
		}

		test.resources.findAll { it.endsWith '.bpmn' } each {

			knowledgeBuilder.add(ResourceFactory.newClassPathResource(it), ResourceType.BPMN2);
		}

		return knowledgeBuilder.newKnowledgeBase().knowledgePackages
	}

	KnowledgeBase createKnowledgeBase() {

		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase()
		knowledgeBase.addKnowledgePackages loadResourcesPackages()
		knowledgeBase.addKnowledgePackages createSubProcessMockPackages()

		return knowledgeBase
	}

	StatefulKnowledgeSession createKnowledgeSession() {

		StatefulKnowledgeSession session = createKnowledgeBase().newStatefulKnowledgeSession()
		session.getWorkItemManager().registerWorkItemHandler 'Human Task', new DoNothingWorkItemHandler()

		return session
	}

	def execute() {

		session = createKnowledgeSession()

		test.when?.closure?.call(session)

		test.when?.actions?.each {

			def methodName = it.remove(0)
			Method method = this.class.methods.find { it.name == methodName }

			method.invoke(this, it.toArray())
		}

		test.then?.call(session)
	}

	def insert(String key, Object object) {

		factHandles.put(key, session.insert(object))
	}

	def startProcess(String processId, Map m) {

		session.startProcess processId, m
	}

	def fireAllRules() {

		session.fireAllRules()
	}

	def executeHumanTasks() {

		def manager = session.workItemManager

		manager.workItems.findAll { it.state == 0 && it.name == 'Human Task' } each {

			test.humanTasks[it.parameters.TaskName].call(it, manager)
		}
	}
}
