export class ErrorResponse {
    code: number;
    message: string;
    type: string;
    data: string;
    constructor(code: number, message: string, type: string, data: string) {
        this.code = code;
        this.message = message;
        this.type = type;
        this.data = data;
    }
}