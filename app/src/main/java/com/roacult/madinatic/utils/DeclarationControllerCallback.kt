package com.roacult.madinatic.utils

import com.roacult.domain.entities.Declaration
import com.roacult.domain.entities.GeoCoordination

interface DeclarationControllerCallback  {

    fun gpsClicked(localisation : GeoCoordination)

    fun readMoreClicked(declaration : Declaration)
}