package com.roacult.domain.repos

import com.roacult.domain.entities.Categorie
import com.roacult.domain.entities.Declaration
import com.roacult.domain.entities.User
import com.roacult.domain.exceptions.AnnounceFailure
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.usecases.announce.AnnouncePage
import com.roacult.domain.usecases.announce.AnnouncePageParam
import com.roacult.domain.usecases.declaration.DeclarationPage
import com.roacult.domain.usecases.declaration.DeclarationPageParam
import com.roacult.domain.usecases.profile.AddDocumentsParams
import com.roacult.domain.usecases.profile.ChangePasswordParam
import com.roacult.domain.usecases.profile.EditInfoParams
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None
import io.reactivex.Observable

interface MainRepo {

    fun getUserObservable(): Observable<Either<ProfileFailures, User>>

    suspend fun editInfo(editUserInfo: EditInfoParams): Either<ProfileFailures, None>

    suspend fun changePassword(changePasswordParam: ChangePasswordParam): Either<ProfileFailures, None>

    suspend fun fetchCategories(): Either<DeclarationFailure, List<Categorie>>

    suspend fun submitDeclaration(declaration: Declaration): Either<DeclarationFailure, None>

    suspend fun fetchDeclrations(page: DeclarationPageParam): Either<DeclarationFailure, DeclarationPage>

    suspend fun fetchUserDeclrations(page: Int): Either<DeclarationFailure, DeclarationPage>

    suspend fun fetchUnfinishedUserDeclrations(page: Int): Either<DeclarationFailure, DeclarationPage>

    suspend fun postDocuments(documentsParams: AddDocumentsParams): Either<DeclarationFailure, None>

    suspend fun deleteDeclaration(declaration: String): Either<DeclarationFailure, None>

    suspend fun fetchAnnouncePage(announcePageParam: AnnouncePageParam): Either<AnnounceFailure, AnnouncePage>
}