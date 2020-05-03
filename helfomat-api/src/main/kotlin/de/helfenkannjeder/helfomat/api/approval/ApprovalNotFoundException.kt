package de.helfenkannjeder.helfomat.api.approval

import de.helfenkannjeder.helfomat.core.approval.ApprovalId

class ApprovalNotFoundException(id: ApprovalId) : RuntimeException("Approval with id $id could not be found")
