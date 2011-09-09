import org.freaknowledge.trip.TripProcess

def test = new org.freaknowledge.protest.ProcessTestBuilder()
def test2 = new org.freaknowledge.protest.ProcessTestBuilder()

test {

	load 'Rule.drl', 'Flow.rf'

	given {

		humanTask 'TaskName', { workItem, manager ->

			manager.completeWorkItem workItem.id, [:]
		}

		subProcess 'SubProcessId', { context ->  }
	}

	when 'Something happens', {

		startProcess 'ProcessId'
		executeHumanTasks()
	}

	then { knowledgeSession -> assert knowledgeSession != null }
}

test2 {

	load 'org/freaknowledge/Rule.drl', 'org/freaknowledge/TripProcess.bpmn'

	given {

		humanTask 'AddReceipts', { workItem, manager ->

			manager.completeWorkItem workItem.id, [:]
		}
	}

	when 'Start TripProcess and execute AddReceipts', {

		insert 'TripProcess', new TripProcess()
		fireAllRules()
	}

	then { knowledgeSession -> assert knowledgeSession != null }
}

new org.freaknowledge.protest.TestRuntime(test: test.test).execute()
new org.freaknowledge.protest.TestRuntime(test: test2.test).execute()