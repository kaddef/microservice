export default function extractToken(header) {
    // Check if header exists
    if (!header) {
        return null;
    }

    // Check if header follows the "Bearer TOKEN" format
    if (!header.startsWith('Bearer ')) {
        return null;
    }

    // Extract and return only the token part
    const parts = header.split(' ');

    // Ensure the header has the correct format (Bearer + token)
    if (parts.length !== 2) {
        return null;
    }

    return parts[1];
}