package org.freaknowledge.protest

import java.util.Map


class ProcessTestBuilder extends BuilderSupport {

	Test test

	ProcessTestBuilder() {

		test = new Test()
		current = createNode('test')
	}

	void load(String[] resources) {

		createNode('load')

		test.resources.addAll resources
	}

	void humanTask(String name, Closure closure) {

		createNode('humanTask')

		test.humanTasks.put name, closure
	}

	void subProcess(String processId, Closure closure) {

		createNode('subProcess')

		test.subProcesses.put processId, closure
	}

	void when(String description, Closure closure) {

		createNode('when')

		test.when = new When(description: description, closure: closure)
	}

	void given(Closure closure) {

		createNode('given')

		closure.call()
	}

	void then(Closure closure) {

		createNode('given')

		test.then = closure
	}

	void insert(String key, Object object) {

		createNode('insert')

		test.when.actions << ['insert', key, object]
	}

	void startProcess(String processId) {

		startProcess processId, [:]
	}

	void startProcess(String processId, Map m) {

		createNode('startProcess')

		test.when.actions << ['startProcess', processId, m]
	}

	void fireAllRules() {

		createNode('fireAllRules')

		test.when.actions << ['fireAllRules']
	}

	void executeHumanTasks() {

		createNode('executeHumanTasks')

		test.when.actions << ['executeHumanTasks']
	}

	void setParent(Object parent, Object child) {
	}

	def createNode(Object name) {

		new Node(current, name, [])
	}

	def createNode(Object name, Object value) {
		createNode(name)
	}

	def createNode(Object name, Map attributes) {
		createNode(name)
	}

	def createNode(Object name, Map attributes, Object value) {
		createNode(name)
	}
}
