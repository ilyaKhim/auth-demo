package ru.ilkhim.exception

class SmartmemoException(message: String) :
    RuntimeException("Exception during interacting with smartmemo. $message")
