package ru.ilkhim.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.ilkhim.persistence.model.UserRole

interface UserRoleRepository : JpaRepository<UserRole, Int>
