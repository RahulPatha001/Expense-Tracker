from dotenv import load_dotenv, dotenv_values
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from pydantic import BaseModel, Field
from langchain_mistralai import ChatMistralAI
import os

from app.service.Expense import Expense

class LLMService:
    def __init__(self):
        load_dotenv()
        self.prompt = ChatPromptTemplate.from_messages(
            [
                (
                    "system",
                    "You are an expert extraction algorithm."
                    "Only extract relevant information from the text."
                    "If you do not know the balue of an attribute asked to extract, "
                    "return null fro the attribute's value."
                ),
                ("human", "{text}")
            ]
        )
        self.apiKey = os.getenv('AI_API_KEY')
        self.llm = ChatMistralAI(api_key=self.apiKey, model="mistral-medium")
        self.runnable = self.prompt | self.llm.with_structured_output(schema=Expense)
    
    def runLLM(self, message):
        return self.runnable.invoke({"text": message})