// Frontend validatie met JOI

import Joi from 'joi';
import { getCsrfInfo } from './modules/csrf.js';

const form = document.getElementById('addSupplierForm');
const nameInput = document.getElementById('name');
const contactPersonInput = document.getElementById('contactPerson');
const emailInput = document.getElementById('email');
const phoneNumberInput = document.getElementById('phoneNumber');
const submitButton = document.querySelector('#addSupplierForm > button');

const keysToInputs = new Map();
keysToInputs.set('name', nameInput);
keysToInputs.set('contactPerson', contactPersonInput);
keysToInputs.set('email', emailInput);
keysToInputs.set('phoneNumber', phoneNumberInput);

const supplierSchema = Joi.object({
    name: Joi.string()
        .required(),
    contactPerson: Joi.string()
        .required(),
    email: Joi.string()
        .email({ tlds: { allow: false } })
        .required(),
    phoneNumber: Joi.string()
        .required()
});

function trySubmitForm() {

    const supplierObject = {
        name: nameInput.value,
        contactPerson: contactPersonInput.value,
        email: emailInput.value,
        phoneNumber: phoneNumberInput.value
    };

    const validationResult = supplierSchema.validate(supplierObject, { abortEarly: false });
    console.log(validationResult);

    nameInput.setCustomValidity('');
    contactPersonInput.setCustomValidity('');
    emailInput.setCustomValidity('');
    phoneNumberInput.setCustomValidity('');

    if (validationResult.error) {
        for (const errorDetail of validationResult.error.details) {
            const inputField = keysToInputs.get(errorDetail.context.key);
            inputField.classList.add('is-invalid');
            inputField.nextElementSibling.innerHTML = errorDetail.message;
        }
        return;
    }

    fetch('/suppliers', { // supplier endpoint
        method: 'POST',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
            ...getCsrfInfo()
        },
        body: JSON.stringify(supplierObject)
    }).then(response => {
        if (response.status === 201) {
            window.location.href = '/suppliers';
        } else {
            if (response.headers.get('Content-Type').includes('application/json')) {
                response.json().then(errorData => {
                    const errors = errorData.errors;
                    errors.forEach(error => {
                        const inputField = keysToInputs.get(error.field);
                        inputField.classList.add('is-invalid');
                        inputField.nextElementSibling.innerHTML = error.defaultMessage;
                    });
                });
            } else {
                console.error('Failed to create supplier');
            }
        }
    });
}

submitButton.addEventListener('click', trySubmitForm);